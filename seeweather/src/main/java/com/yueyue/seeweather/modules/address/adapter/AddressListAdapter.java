/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yueyue.seeweather.modules.address.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yueyue.seeweather.R;
import com.yueyue.seeweather.modules.address.domain.City;
import com.yueyue.seeweather.modules.address.listener.OnCompatItemClickListener;

import java.util.List;

/**
 * author : yueyue on 2018/3/2 00:52
 * desc   : 地址列表的适配器。
 * quote  : https://github.com/yanzhenjie/AddressChecker
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<City> mCityList;
    private OnCompatItemClickListener mItemClickListener;

    public AddressListAdapter(LayoutInflater layoutInflater, OnCompatItemClickListener itemClickListener) {
        this.mLayoutInflater = layoutInflater;
        this.mItemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(List<City> mCityList) {
        this.mCityList = mCityList;
        super.notifyDataSetChanged();
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_address_select, parent, false);
        AddressViewHolder viewHolder = new AddressViewHolder(view);
        viewHolder.mItemClickListener = mItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        holder.setData(mCityList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCityList == null ? 0 : mCityList.size();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvAddress;
        RadioButton mRadioButton;

        OnCompatItemClickListener mItemClickListener;

        AddressViewHolder(View itemView) {
            super(itemView);
            mTvAddress = (TextView) itemView.findViewById(R.id.tv_area_name);
            mRadioButton = (RadioButton) itemView.findViewById(R.id.radio_btn);

            itemView.setOnClickListener(this);
        }

        public void setData(City mData) {
            mTvAddress.setText(mData.getName());
            mRadioButton.setChecked(mData.isSelect());
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

}
