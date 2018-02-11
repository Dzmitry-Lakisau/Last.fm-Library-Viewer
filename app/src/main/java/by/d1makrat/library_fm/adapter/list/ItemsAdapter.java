package by.d1makrat.library_fm.adapter.list;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.R;

public abstract class ItemsAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LongClickListener{

    private static final int HEADER = 0;
    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    private static final int ERROR_HEADER = 3;
    private static final int EMPTY_HEADER = 4;

    private boolean isFooterAdded = false;
    private boolean isHeaderAdded = false;
    private boolean isErrorHeaderAdded = false;
    private boolean isEmptyHeaderAdded = false;

    private final T t = null;

    Drawable mPlaceholderDrawable;
    LayoutInflater mLayoutInflater;
    private final List<T> mItems = new ArrayList<>();
    private int mSelectedItemPosition;
    private String message;

    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isHeaderAdded){
            return HEADER;
        }
        else if (position == mItems.size()-1 && isFooterAdded){
            return FOOTER;
        }
        else if (position == 0 && isErrorHeaderAdded){
            return ERROR_HEADER;
        }
        else if (position == 0 && isEmptyHeaderAdded){
            return EMPTY_HEADER;
        }
        else return ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case HEADER:
                viewHolder = createHeaderViewHolder(parent);
                break;
            case ITEM:
                viewHolder = createItemViewHolder(parent);
                break;
            case FOOTER:
                viewHolder = createFooterViewHolder(parent);
                break;
            case ERROR_HEADER:
                viewHolder = createErrorHeaderViewHolder(parent);
                break;
            case EMPTY_HEADER:
                viewHolder = createEmptyHeaderViewHolder(parent);
                break;
            default:
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                bindItemViewHolder(viewHolder, position);
                break;
            case EMPTY_HEADER:
                bindEmptyHeaderViewHolder(viewHolder, message);
                break;
        }
    }

    private RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.item_spinner_full, parent, false);

        return new HeaderViewHolder(v);
    }

    protected abstract RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent);

    private RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.item_spinner, parent, false);

        return new FooterViewHolder(v);
    }

    private RecyclerView.ViewHolder createErrorHeaderViewHolder(ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.item_error_header, parent, false);

        return new ErrorHeaderViewHolder(v);
    }

    private RecyclerView.ViewHolder createEmptyHeaderViewHolder(ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.item_empty_header, parent, false);

        return new EmptyHeaderViewHolder(v);
    }

    protected abstract void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    private void bindEmptyHeaderViewHolder(RecyclerView.ViewHolder viewHolder, String message){
        final EmptyHeaderViewHolder holder = (EmptyHeaderViewHolder) viewHolder;
        holder.bind(message);
    }

    protected T getItem(int position){
        return mItems.get(position);
    }

    public T getSelectedItem() {
        return mItems.get(mSelectedItemPosition);
    }

    private void add(T item){
        mItems.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addAll(List<T> items){
        for (T item : items){
            add(item);
        }
    }

    private void remove(T item) {
        int position = mItems.indexOf(item);
        if (position > -1) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
        isHeaderAdded = false;
        isFooterAdded = false;
        isEmptyHeaderAdded = false;
        isErrorHeaderAdded = false;
    }

    public void addFooter() {
        isFooterAdded = true;
        add(t);
    }

    private void removeFooter(){
        if (isFooterAdded) {
            int position = getItemCount() - 1;
            mItems.remove(position);
            notifyItemRemoved(position);
            isFooterAdded = false;
        }
    }

    public void addHeader() {
        isHeaderAdded = true;
        add(t);
    }

    private void removeHeader(){
        if (isHeaderAdded) {
            int position = 0;
            mItems.remove(position);
            notifyItemRemoved(position);
            isHeaderAdded = false;
        }
    }

    public void addErrorHeader() {
        isErrorHeaderAdded = true;
        add(t);
    }

    private void removeErrorHeader(){
        if (isErrorHeaderAdded) {
            int position = 0;
            mItems.remove(position);
            notifyItemRemoved(position);
            isErrorHeaderAdded = false;
        }
    }

    public void addEmptyHeader(String message) {
        isEmptyHeaderAdded = true;
        this.message = message;
        add(t);
    }

    private void removeEmptyHeader(){
        if (isEmptyHeaderAdded) {
            int position = 0;
            mItems.remove(position);
            notifyItemRemoved(position);
            isEmptyHeaderAdded = false;
        }
    }

    public void removeAllHeadersAndFooters() {
        removeHeader();
        removeFooter();
        removeErrorHeader();
        removeEmptyHeader();
    }

    @Override
    public void onItemLongClick(int position) {
        mSelectedItemPosition = position;
    }

    public boolean isEmpty(){
        return !(getItemViewType(0) == ITEM) || mItems.size() == 0;
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder {

        FooterViewHolder(View view) {
            super(view);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(View view) {
            super(view);
        }
    }

    private static class ErrorHeaderViewHolder extends RecyclerView.ViewHolder {

        ErrorHeaderViewHolder(View view) {
            super(view);

        }
    }

    protected static class EmptyHeaderViewHolder extends RecyclerView.ViewHolder {

        final TextView message_TextView;

        EmptyHeaderViewHolder(View view) {
            super(view);

            message_TextView = view.findViewById(R.id.item_empty_header_message);
        }

        void bind(String message){
            message_TextView.setText(message);
        }
    }
}
