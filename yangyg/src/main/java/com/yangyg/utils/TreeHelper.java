package com.yangyg.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.yangyg.annotation.TreeNodeId;
import com.yangyg.annotation.TreeNodeName;
import com.yangyg.annotation.TreeNodePId;


import android.content.Context;
import android.util.Log;

public class TreeHelper {

	/**
	 * 通过反射机制将用户的数据转换为树形数据
	 * @param datas
	 * @return
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private static <T> List<Node> convertDatas2Nodes(List<T> datas,Context context) throws IllegalAccessException, IllegalArgumentException{
		
		List<Node> nodes = new ArrayList<Node>();
		Node node = null;
		for(T t : datas){
			//获取这个类
			Class clazz = t.getClass();
			//获得这个类的所有字段
			Field[] fields = clazz.getDeclaredFields();
			int id = -1;
			int pid = -1;
			String name = null;
			for(Field field : fields){
				if(field.getAnnotation(TreeNodeId.class) != null){
					field.setAccessible(true);//设置访问权限  默认为false（外部不可使用）
					id = field.getInt(t);
				}
				if(field.getAnnotation(TreeNodePId.class) != null){
					field.setAccessible(true);//设置访问权限  默认为false（外部不可使用）
					pid = field.getInt(t);
				}
				if(field.getAnnotation(TreeNodeName.class) != null){
					field.setAccessible(true);//设置访问权限  默认为false（外部不可使用）
					name = (String) field.get(t);
				}
			}
			node = new Node(id, pid, name);
			nodes.add(node);
		}
		
		/**
		 * 设置Node间的节点关系
		 */
		for(int i=0;i<nodes.size();i++){
			Node n = nodes.get(i);
			
			for(int j=i+1;j<nodes.size();j++){
				Node m = nodes.get(j);
				if(m.getpId() == n.getId()){
					n.getChildren().add(m);
					m.setParent(n);
				}else if(m.getId() == n.getpId()){
					m.getChildren().add(n);
					n.setParent(m);
				}
			}
		}
		
		for(Node n : nodes){
			setNodeIcon(n,context);
		}
		
		return nodes;
	}

	/**
	 * 为Node设置图标
	 * @param n
	 */
	private static void setNodeIcon(Node n,Context context) {
		// TODO Auto-generated method stub
		if(n.getChildren().size() > 0 && n.isExpard()){//tree_close_icon  tree_open_icon
			n.setIcon(MResource.getIdByName(context, "drawable", "tree_open_icon"));
		}else if(n.getChildren().size() > 0 && !n.isExpard()){
			n.setIcon(MResource.getIdByName(context, "drawable", "tree_close_icon"));
		}else{
			n.setIcon(-1);
		}
			
	}
	
	/**
	 * 获得排序后的节点
	 * @param datas
	 * @param defaultExpandLevel
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static <T> List<Node> getSortedNodes(List<T> datas,int defaultExpandLevel,Context context) throws IllegalAccessException, IllegalArgumentException{
		
		List<Node> result = new ArrayList<Node>();
		List<Node> nodes = convertDatas2Nodes(datas, context);
		
		//获取树的根节点
		List<Node> rootNodes = getRootNodes(nodes);
		
		for(Node node : rootNodes){
			Log.e("yyg", "-----根节点------->"+node.getName());
			addNode(result,node,defaultExpandLevel,1);
		}
		Log.e("yyg", "-----ss------->"+result.size());
		return result;
	}

	/**
	 * 把一个节点的所有孩子节点放入result
	 * @param result
	 * @param node
	 * @param defaultExpandLevel
	 * @param i
	 */
	private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
		// TODO Auto-generated method stub
		result.add(node);
		Log.e("yyg", "--------添加的节点---》"+node.getName());
		if(node.isLeaf()){
			return;
		}
		if(defaultExpandLevel >= currentLevel){
			node.setExpard(true);
		}
		for(int i=0;i<node.getChildren().size();i++){
			addNode(result, node.getChildren().get(i), defaultExpandLevel, currentLevel+1);
		}
	}
	
	/**
	 * 过滤出可见的节点
	 * @param nodes
	 * @return
	 */
	public static List<Node> filterVisibleNodes(List<Node> nodes,Context context){
		List<Node> result = new ArrayList<Node>();
		for(Node node : nodes){
			if(node.isRoot() || node.isParentExpand()){
				setNodeIcon(node, context);
				result.add(node);
			}
		}
		return result;
	}

	/**
	 * 从所有节点中获得根节点
	 * @param nodes
	 * @return
	 */
	private static List<Node> getRootNodes(List<Node> nodes) {
		List<Node> root = new ArrayList<Node>();
		for(Node node : nodes){
			if(node.isRoot()){
				root.add(node);
			}
		}
		return root;
	}
}
