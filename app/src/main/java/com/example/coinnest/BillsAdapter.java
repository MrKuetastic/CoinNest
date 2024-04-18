package com.example.coinnest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillViewHolder> {
    private List<Bill> bills;
    private Context context;
    private OnBillListener onBillListener;

    public BillsAdapter(List<Bill> bills, Context context, OnBillListener onBillListener) {
        this.bills = bills;
        this.context = context;
        this.onBillListener = onBillListener;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        Bill bill = bills.get(position);
        holder.textViewBillName.setText(bill.getBillName());
        holder.textViewBillDueDate.setText(bill.getDueDate());
        holder.textViewBillCategory.setText(bill.getCategory());

        // Using locale-specific currency formatting
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.textViewBillAmount.setText(currencyFormat.format(bill.getAmount()));

        // Set the click listener for the edit button with the current bill
        holder.buttonEditBill.setOnClickListener(v -> {
            Log.d("EditAction", "Button click registered for: " + bill.getBillName());
            if (onBillListener != null) {
                onBillListener.onEditBill(bill);
            } else {
                Log.d("EditAction", "Listener is null");
            }
        });
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public interface OnBillListener {
        void onEditBill(Bill bill);
    }

    static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBillName, textViewBillAmount, textViewBillDueDate, textViewBillCategory;
        Button buttonEditBill;

        public BillViewHolder(View itemView) {
            super(itemView);
            textViewBillName = itemView.findViewById(R.id.textViewBillName);
            textViewBillAmount = itemView.findViewById(R.id.textViewBillAmount);
            textViewBillDueDate = itemView.findViewById(R.id.textViewBillDueDate);
            textViewBillCategory = itemView.findViewById(R.id.textViewBillCategory);
            buttonEditBill = itemView.findViewById(R.id.buttonEditBill);
        }
    }
}
