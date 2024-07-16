package com.example.mapdemo.data.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.FirebaseBooking;
import com.example.mapdemo.helper.CallbackHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class FirebaseBookingRepositoryImpl implements FirebaseBookingRepository {

    String firebaseInstance;
    @Inject
    public FirebaseBookingRepositoryImpl(String firebaseInstance){
        this.firebaseInstance = firebaseInstance;

    }
    @Override
    public void addFirebaseBooking(FirebaseBooking firebaseBooking) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseInstance);
        DatabaseReference databaseReference = database.getReference("bookings");
        databaseReference.child(firebaseBooking.getIdBooking()).setValue(firebaseBooking);
    }

    @Override
    public void getBookedRoomByTime(String idAccom, Date startDate, Date endDate, CallbackHelper callback) {
        final int[] bookedRoom = {0};
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseInstance);
        DatabaseReference databaseReference = database.getReference("bookings");
        databaseReference.orderByChild("idTarget").equalTo(idAccom).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int bookedRoom = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseBooking booking = snapshot.getValue(FirebaseBooking.class);
                    if (booking != null && !isBookingWithinRange(booking, startDate, endDate)) {
                        bookedRoom += booking.getNumOfRooms();
                    }
                }
                callback.onDataReceived(bookedRoom);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching bookings: " + databaseError.getMessage());
            }
        });
    }
    private boolean isBookingWithinRange(FirebaseBooking booking, Date startDate, Date endDate) {
        long startTimestamp = startDate.getTime();
        long endTimestamp = endDate.getTime();
        long bookingStart = booking.getStartDay();
        long bookingEnd = booking.getEndDay();
        return (bookingEnd <= startTimestamp || bookingStart >= endTimestamp);
    }
}
