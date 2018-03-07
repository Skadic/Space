package sebet.space.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sebet.space.R;
import sebet.space.list.tasks.Task;
import sebet.space.list.tasks.TaskDrug;
import sebet.space.list.tasks.TaskPump;
import sebet.space.list.viewholder.ViewHolderTaskDrug;
import sebet.space.list.viewholder.ViewHolderTaskPump;
import sebet.space.listener.OnItemClickListener;
import sebet.space.utils.EnumTasks;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EnumTasks> taskList;
    private OnItemClickListener listener;
    private Context context;

    public ListAdapter(Context context, List<EnumTasks> taskList) {
        this.taskList = taskList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public List<EnumTasks> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<EnumTasks> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public void add(int position, EnumTasks task) {
        taskList.add(position, task);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View v2 = inflater.inflate(R.layout.item_task, parent, false);
                return new ViewHolderTaskPump(context, v2);
            case 2:
                View v3 = inflater.inflate(R.layout.item_task, parent, false);
                return new ViewHolderTaskDrug(context, v3);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case 1:
                ((ViewHolderTaskPump) holder).setItem(taskList.get(position), listener);
                break;
            case 2:
                ((ViewHolderTaskDrug) holder).setItem(taskList.get(position), listener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (taskList.get(position)) {
            case PUMP:
                return 1;
            case DRUG_CONTAINER:
                return 2;
        }
        return 0;
    }

}

