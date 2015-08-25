package com.pami.adapter;

import java.util.List;

import com.pami.utils.Node;
import com.pami.utils.TreeHelper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */
public abstract class PMTreeListViewAdapter<T> extends BaseAdapter {

	protected Context mContext;
	/**所有的节点*/
	protected List<Node> mAllNodes;
	/**显示的节点*/
	protected List<Node> mVisibleNodes;
	
	protected LayoutInflater mInflater;
	
	protected ListView mTree;
	/**点击树节点的监听器*/
	protected OnTreeItemClickListener mListener;
	
	public void setOnTreeItemClickListener(OnTreeItemClickListener mListener) {
		this.mListener = mListener;
	}

	public interface OnTreeItemClickListener{
		void onTreeItemClick(Node node,int position);
	}
	
	public PMTreeListViewAdapter(ListView tree,Context context,List<T> datas,int defaultExpandLevel) throws IllegalAccessException, IllegalArgumentException{
		
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel,context);
		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes,context);
		mTree = tree;
		mTree.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				expandOrCollapse(position);
				if(mListener != null){
					mListener.onTreeItemClick(mVisibleNodes.get(position) , position);
				}
			}
		});
		
	}
	
	/**
	 * 点击收缩或者展开
	 * @param position
	 */
	protected void expandOrCollapse(int position) {
		// TODO Auto-generated method stub
		Node n = mVisibleNodes.get(position);
		if(n != null){
			if(n.isLeaf())return;
			n.setExpard(!n.isExpard());
			
			mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes,mContext);
			notifyDataSetChanged();
		}
	}
	@Override
	public int getCount() {
		if(mVisibleNodes == null){
			return 0;
		}
		return mVisibleNodes.size();
	}

	@Override
	public Object getItem(int position) {
		return mVisibleNodes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Node node = mVisibleNodes.get(position);
		//设置内边距
		convertView = getConvertView(node, position, convertView, parent);
		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
		return convertView;
	}
	
	public abstract View getConvertView(Node node,int position,View convertView,ViewGroup parent);

	/**
	 * 动态插入节点
	 * @param position
	 * @param string
	 */
	public void addExtraNode(int position, String string) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(string)){
			return;
		}
		Node node = mVisibleNodes.get(position);
		int index = mAllNodes.indexOf(node);
		
		Node extraNode = new Node(-1, node.getId(), string);
		extraNode.setParent(node);
		node.getChildren().add(extraNode);
		mAllNodes.add(index+1, extraNode);
		
		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes,mContext);
		notifyDataSetChanged();
	}
}
