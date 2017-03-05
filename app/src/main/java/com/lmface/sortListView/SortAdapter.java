package com.lmface.sortListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lmface.R;

import java.util.List;

import static com.lmface.signin.SponporSignInActivity.list_userName;

public class SortAdapter extends BaseAdapter implements SectionIndexer {

	private List<SortModel> list = null;
	
	private Context mContext;

	private ViewHolder viewHolder = null;
	private MyCheckedChangeListener listener;

	public static boolean mAllChecked=false;
	public static boolean mIsAllChecked=false;
	
	public SortAdapter(Context mContext,List<SortModel> list){
		this.mContext = mContext;
		this.list = list;
		this.listener=(MyCheckedChangeListener) mContext;

	}

	private void setMyCheckedChangeListener(MyCheckedChangeListener listener) {
		this.listener = listener;
	}

	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public interface MyCheckedChangeListener {
		public void setBtnText();
	}
	

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		

		final SortModel mContent = list.get(position);

		if (convertView== null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.signperson_list_item, null);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			viewHolder.cb = (SmoothCheckBox) convertView.findViewById(R.id.scb);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.cb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
				mContent.isChecked = isChecked;

				if (isChecked){
					list_userName.add(mContent.getName());
				}else {
					list_userName.remove(mContent.getName());
				}
				listener.setBtnText();

				Log.i("gqf", "setOnCheckedChangeListener---list_userName.size()---" + list_userName.size());
				for (int i = 0; i < list_userName.size(); i++) {

					Log.i("gqf", "setOnCheckedChangeListener---list_userName.size()---" + list_userName.get(i));
				}


			}
		});
		Log.e("Daniel","---mAllChecked----"+mAllChecked);
		Log.e("Daniel","---mIsAllChecked----"+mIsAllChecked);
		//是否点击全选

		if (mIsAllChecked){
			if (!mAllChecked){
				viewHolder.cb.setChecked(false);
				list_userName.clear();
			}else {
				if (position==0&&list_userName.size()!=0){
					list_userName.clear();
				}
				viewHolder.cb.setChecked(true);
			}
			if (position==(list.size()-1)){
				mIsAllChecked = false;
			}
		}else {
			viewHolder.cb.setChecked(mContent.isChecked);
		}





//		if (viewHolder.cb.isChecked()){
//			list_userName.clear();
//			list_userName.add(mContent.getName());
//			Log.e("Daniel","--getView-list_userName.size()----"+ list_userName.size());
//		}
		//����position��ȡ���������ĸ��Char asciiֵ
		int section = getSectionForPosition(position);
		//�����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(this.list.get(position).getName());


		return convertView;
	}

	public  void setAllCheckBoxChecked(){
		mIsAllChecked = true;
		if (mAllChecked){
			mAllChecked = false;
		}else {
			mAllChecked = true;
		}
		notifyDataSetChanged();


	}



	@Override
	public Object[] getSections() {
		return null;
	}
	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == sectionIndex) {
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
	 */
	@Override
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	
	final static class ViewHolder{
		TextView tvLetter;
		TextView tvTitle;
		SmoothCheckBox cb;
	}
	/**
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
//	class Bean implements Serializable {
//		boolean isChecked;
//	}

	
}
