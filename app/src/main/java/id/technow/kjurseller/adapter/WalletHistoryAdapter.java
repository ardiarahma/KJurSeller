package id.technow.kjurseller.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.technow.kjurseller.R;
import id.technow.kjurseller.model.WalletLog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.CustomViewHolder>{

    List<WalletLog> walletLogs;

    public WalletHistoryAdapter(List<WalletLog> walletLogs) {
        this.walletLogs = walletLogs;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_wallet_history, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        WalletLog walletLog = walletLogs.get(position);

        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator('.');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);

        holder.txtRef.setText(String.valueOf(walletLog.getReferenceId()));
        holder.txtDateTime.setText(walletLog.getDateTime());
        holder.txtTotal.setText(String.valueOf(fmt.format(walletLog.getTotal())));
        holder.txtType.setText(walletLog.getType());
        holder.txtBalance.setText(String.valueOf(fmt.format(walletLog.getGrandTotal())));
    }

    @Override
    public int getItemCount() {
        return walletLogs.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView txtRef, txtDateTime, txtTotal, txtType, txtBalance;

        public CustomViewHolder(View view) {
            super(view);
            txtRef = itemView.findViewById(R.id.txtRef);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtType = itemView.findViewById(R.id.txtType);
            txtBalance = itemView.findViewById(R.id.txtBalance);
        }
    }
}