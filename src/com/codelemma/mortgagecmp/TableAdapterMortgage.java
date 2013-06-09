package com.codelemma.mortgagecmp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class TableAdapterMortgage extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<TableGroupMortgage> groups;
	
	public TableAdapterMortgage(Context context, ArrayList<TableGroupMortgage> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(TableChildMortgage item, TableGroupMortgage group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<TableChildMortgage> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<TableChildMortgage> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		TableChildMortgage group = (TableChildMortgage) getChild(groupPosition, childPosition);
		View childV = populateView(view, group);
		childV.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
		return childV;
	}
	
	private View populateView(View view, TableMortgage group) {
		
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) 
					context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.list_mortgage, null);
		}
		
		TextView tv;
		tv = (TextView) view.findViewById(R.id.date);
		tv.setText(group.getDate().toString());
												
		tv = (TextView) view.findViewById(R.id.interests_paid);
		tv.setText(group.getInterestsPaid().toString());
		
		tv = (TextView) view.findViewById(R.id.monthly_total);
		tv.setText(group.getMonthlyPayment().toString());

		tv = (TextView) view.findViewById(R.id.additional_cost);
		tv.setText(group.getAdditionalCost().toString());
		
		tv = (TextView) view.findViewById(R.id.principal_paid);
		tv.setText(group.getPrincipalPaid().toString());
				
		tv = (TextView) view.findViewById(R.id.remaining_amount);
		tv.setText(group.getRemainingAmount().toString());

						
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<TableChildMortgage> chList = groups.get(groupPosition).getItems();
		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		TableGroupMortgage group = (TableGroupMortgage) getGroup(groupPosition);
		return populateView(view, group);
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
}
