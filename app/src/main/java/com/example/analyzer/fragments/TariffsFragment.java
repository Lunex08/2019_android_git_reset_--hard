package com.example.analyzer.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.analyzer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class TariffsFragment extends Fragment {
    private static List<String> tariffNames;
    private static List<String> tariffGigabytes;
    private static List<String> tariffSMS;
    private static List<String> tariffPrices;
    private static List<Integer> tariffIcons;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tariffs, container, false);

        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tariffs);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        tariffNames = new ArrayList<>();
        tariffGigabytes = new ArrayList<>();
        tariffSMS = new ArrayList<>();
        tariffPrices = new ArrayList<>();
        tariffIcons = new ArrayList<>();

        final RecyclerView recyclerView = view.findViewById(R.id.tariffs_content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(tariffNames, tariffGigabytes, tariffSMS, tariffPrices, tariffIcons);
        recyclerView.setAdapter(recyclerAdapter);

        loadJSON(recyclerAdapter, getResources().getString(R.string.api_url));

        return view;
    }

    private void loadJSON(RecyclerAdapter adapter, String url) {
        new TariffAsyncTask(adapter, url).execute();
    }

        class RecyclerViewHolder extends RecyclerView.ViewHolder {
            private final TextView name;
            private final TextView gigabyte;
            private final TextView sms;
            private final TextView price;
            private final ImageView icon;

            private RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.tariffs_name);
                gigabyte = itemView.findViewById(R.id.tariffs_gigabytes);
                sms = itemView.findViewById(R.id.tariffs_sms);
                price = itemView.findViewById(R.id.tariffs_price);
                icon = itemView.findViewById(R.id.tariffs_operator_icon);
            }
        }

        class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
            private final List<String> names;
            private final List<String> gigabytes;
            private final List<String> smsOne;
            private final List<String> prices;
            private final List<Integer> icons;

            private RecyclerAdapter(@NonNull List<String> names, @NonNull List<String> gigabytes, @NonNull List<String> sms, @NonNull List<String> prices, @NonNull List<Integer> icons) {
                this.names = names;
                this.gigabytes = gigabytes;
                this.smsOne = sms;
                this.prices = prices;
                this.icons = icons;
            }

            @NonNull
            @Override
            public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tariffs_item, parent, false);
                return new RecyclerViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.name.setText(String.valueOf(names.get(position)));
                holder.gigabyte.setText(String.valueOf(gigabytes.get(position)));
                holder.sms.setText(String.valueOf(smsOne.get(position)));
                holder.price.setText(String.valueOf(prices.get(position)));

                switch (icons.get(position)) {
                    case 1:
                        holder.icon.setBackground(getResources().getDrawable(R.drawable.yota, null));
                        break;
                    case 2:
                        holder.icon.setBackground(getResources().getDrawable(R.drawable.mtc, null));
                        break;
                    case 3:
                        holder.icon.setBackground(getResources().getDrawable(R.drawable.beeline, null));
                        break;
                }
            }

            @Override
            public int getItemCount() {
                return names.size();
            }
        }

        static class TariffAsyncTask extends AsyncTask<String, String, Void> {
            private RecyclerAdapter adapter;
            private final String url;

            private TariffAsyncTask(RecyclerAdapter adapter, String url) {
                this.adapter = adapter;
                this.url = url;
            }

            @Override
            protected Void doInBackground(String... strings) {
                HttpsURLConnection con = null;
                try {
                    URL u = new URL(url);

                    con = (HttpsURLConnection) u.openConnection();
                    con.connect();

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        final String tmp = line + "\n";
                        sb.append(tmp);
                    }

                    br.close();

                    String result = sb.toString();

                    JSONArray jArray = new JSONArray(result);

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        tariffNames.add(jObject.getString("tariff_name"));
                        tariffGigabytes.add(String.valueOf(jObject.getInt("traffic")));
                        tariffSMS.add(String.valueOf(jObject.getInt("sms")));

                        Double price = BigDecimal.valueOf(jObject.getDouble("price"))
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue();

                        tariffPrices.add(String.valueOf(price));
                        tariffIcons.add(jObject.getInt("operator_id"));
                    }

                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    return null;
                } finally {
                    if (con != null) {
                        try {
                            con.disconnect();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                adapter.notifyDataSetChanged();
            }
        }
    }