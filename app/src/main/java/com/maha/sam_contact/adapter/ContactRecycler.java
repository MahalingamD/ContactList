package com.maha.sam_contact.adapter;


import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maha.sam_contact.MainActivity;
import com.maha.sam_contact.R;
import com.maha.sam_contact.model.ContactModel;

import java.util.List;

public class ContactRecycler extends RecyclerView.Adapter<ContactRecycler.ViewHolder> {

   List<ContactModel> mContactList;
   MainActivity mContext;

   public ContactRecycler( MainActivity aContext, List<ContactModel> aContactList ) {
      mContactList = aContactList;
      mContext = aContext;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int i ) {
      return new ViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.contact_item_view, parent, false ) );
   }

   @Override
   public void onBindViewHolder( @NonNull ViewHolder holder, int i ) {
      ContactModel aContactModel = mContactList.get( i );

      holder.mContactName.setText( aContactModel.name );
      holder.mContactNumber.setText( aContactModel.mobileNumber );
      if( aContactModel.photo != null )
         holder.mContactImage.setImageBitmap( aContactModel.photo );
      else {
         holder.mContactImage.setImageDrawable( ContextCompat.getDrawable( mContext, R.mipmap.ic_launcher_round ) );
      }

   }

   @Override
   public int getItemCount() {
      return mContactList.size();
   }

   public class ViewHolder extends RecyclerView.ViewHolder {

      ImageView mContactImage;
      TextView mContactName, mContactNumber;

      public ViewHolder( @NonNull View itemView ) {
         super( itemView );
         mContactImage = itemView.findViewById( R.id.contact_image );
         mContactName = itemView.findViewById( R.id.contact_name );
         mContactNumber = itemView.findViewById( R.id.contact_number );
      }
   }
}
