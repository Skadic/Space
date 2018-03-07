package sebet.space.list.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import sebet.space.R;
import sebet.space.Space;
import sebet.space.decoding.FormatDecoder;
import sebet.space.list.tasks.TaskDrug;
import sebet.space.list.tasks.TaskPump;
import sebet.space.listener.OnBarcodeReceivedListener;
import sebet.space.listener.OnItemClickListener;
import sebet.space.utils.EnumTasks;

import static sebet.space.Space.barcodeCapture;

public class ViewHolderTaskPump extends RecyclerView.ViewHolder {
    public View view;
    public Context context;

    public AppCompatImageView iv_check, iv_uncheck, iv_barcode;
    public TextView tv_main;

    public ViewHolderTaskPump(Context context, View view) {
        super(view);
        this.view = view;
        this.context = context;

        this.iv_check = this.view.findViewById(R.id.iv_check);
        this.iv_uncheck = this.view.findViewById(R.id.iv_uncheck);
        this.iv_barcode = this.view.findViewById(R.id.iv_barcode);
        this.tv_main = this.view.findViewById(R.id.tv_main);
    }

    public void setItem(final EnumTasks task, final OnItemClickListener listener) {
        tv_main.setText("Pumpe");
        iv_barcode.setImageResource(R.drawable.ic_barcode);

        FormatDecoder decoder = Space.decoder;
        if(task == EnumTasks.PUMP ? decoder.hasPump() : decoder.hasDrug()) {
            iv_check.setVisibility(View.VISIBLE);
            iv_uncheck.setVisibility(View.INVISIBLE);
        } else {
            iv_check.setVisibility(View.INVISIBLE);
            iv_uncheck.setVisibility(View.VISIBLE);
        }

        if(task == Space.activeTask) {
            iv_barcode.setColorFilter(ContextCompat.getColor(context, R.color.backcolor_selected));
            iv_uncheck.setColorFilter(ContextCompat.getColor(context, R.color.backcolor_selected));
            tv_main.setTextColor(context.getResources().getColor(R.color.textcolor_main));
            tv_main.setTypeface(null, Typeface.BOLD);
        } else {
            iv_barcode.setColorFilter(ContextCompat.getColor(context, R.color.backcolor_unselected));
            iv_uncheck.setColorFilter(ContextCompat.getColor(context, R.color.backcolor_unselected));
            tv_main.setTextColor(context.getResources().getColor(R.color.textcolor_second));
            tv_main.setTypeface(null, Typeface.NORMAL);
        }

        if(listener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(task);
                }
            });
    }

}

