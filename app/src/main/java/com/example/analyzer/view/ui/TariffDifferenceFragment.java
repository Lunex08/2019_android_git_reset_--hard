package com.example.analyzer.view.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.analyzer.R;
import com.example.analyzer.service.model.EventListener;
import com.example.analyzer.service.model.TariffDataset;
import com.example.analyzer.viewmodel.TariffDifferenceViewModel;

import java.util.List;

public class TariffDifferenceFragment extends Fragment {
    private EventListener eventListener;
    private TariffDifferenceViewModel viewModel;
    private List<TariffDataset> tariffs;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        eventListener = (EventListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventListener = null;
    }

    void setTariffs(List<TariffDataset> tariffs) {
        this.tariffs = tariffs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tariff_difference, container, false);

        viewModel = new ViewModelProvider(this).get(TariffDifferenceViewModel.class);
        viewModel.setTariffs(tariffs);

        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tariffs);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            eventListener.showInfoFragment();
            return true;
        });

        final TariffDataset currentTariff = viewModel.getCurrentTariff();

        final TextView oper_left = view.findViewById(R.id.diff_oper_left);
        oper_left.setText(currentTariff.getOperator());

        final TextView tariff_left = view.findViewById(R.id.diff_tariff_left);
        tariff_left.setText(currentTariff.getName());

        final TextView gb_left = view.findViewById(R.id.diff_gb_left);
        gb_left.setText(currentTariff.getGigabytes() + " ГБ");

        final TextView sms_left = view.findViewById(R.id.diff_sms_left);
        sms_left.setText(currentTariff.getSms() + " смс");

        final TextView price_left = view.findViewById(R.id.diff_price_left);
        price_left.setText(currentTariff.getPrice() + " р/мес");

        final ImageView icon_left = view.findViewById(R.id.diff_oper_icon_left);
        icon_left.setImageResource(TariffDataset.getImageResource(currentTariff.getOperator()));
        icon_left.setContentDescription(currentTariff.getOperator());

        final Bundle bundle = getArguments();
        TariffDataset proposeTariff = viewModel.getProposeTariff(bundle);

        final TextView oper_right = view.findViewById(R.id.diff_oper_right);
        oper_right.setText(proposeTariff.getOperator());

        final ImageView icon_right = view.findViewById(R.id.diff_oper_icon_right);
        icon_right.setImageResource(TariffDataset.getImageResource(proposeTariff.getOperator()));
        icon_right.setContentDescription(proposeTariff.getOperator());

        final TextView tariff_right = view.findViewById(R.id.diff_tariff_right);
        tariff_right.setText(proposeTariff.getName());

        final TextView gb_right = view.findViewById(R.id.diff_gb_right);
        gb_right.setText(proposeTariff.getGigabytes());

        final TextView sms_right = view.findViewById(R.id.diff_sms_right);
        sms_right.setText(proposeTariff.getSms());

        final TextView price_right = view.findViewById(R.id.diff_price_right);
        price_right.setText(proposeTariff.getPrice());

        final TextView profit = view.findViewById(R.id.diff_profit_value);
        profit.setText(viewModel.getProfit(proposeTariff));

        return view;
    }


}