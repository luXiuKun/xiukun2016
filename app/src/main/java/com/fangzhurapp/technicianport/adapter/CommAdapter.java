package com.fangzhurapp.technicianport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * adapter抽取
 * @param <T>
 */
public abstract class CommAdapter<T> extends BaseAdapter
{
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	private int layoutId;

	public CommAdapter(Context context, int layoutId, List<T> datas )
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	public int getSelectPosition(){
		
		return selectPosition;
	}

	private int selectPosition;
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
				layoutId, position);
		selectPosition = position;
		convert(holder, getItem(position),position);
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T t,int position);
	
	
	
	
	

}
