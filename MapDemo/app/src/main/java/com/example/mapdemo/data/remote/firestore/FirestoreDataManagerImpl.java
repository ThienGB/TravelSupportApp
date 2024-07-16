package com.example.mapdemo.data.remote.firestore;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.mapdemo.helper.FirestoreHelper.CL_ACCOM;
import static com.example.mapdemo.helper.FirestoreHelper.CL_FAVORITE;
import static com.example.mapdemo.helper.FirestoreHelper.FL_ACCOMID;
import static com.example.mapdemo.helper.FirestoreHelper.FL_ADDRESS;
import static com.example.mapdemo.helper.FirestoreHelper.FL_CITYID;
import static com.example.mapdemo.helper.FirestoreHelper.FL_DESCRIPTION;
import static com.example.mapdemo.helper.FirestoreHelper.FL_FREEROOM;
import static com.example.mapdemo.helper.FirestoreHelper.FL_IMAGE;
import static com.example.mapdemo.helper.FirestoreHelper.FL_LATITUDE;
import static com.example.mapdemo.helper.FirestoreHelper.FL_LONGTITUDE;
import static com.example.mapdemo.helper.FirestoreHelper.FL_NAME;
import static com.example.mapdemo.helper.FirestoreHelper.FL_PRICE;
import static com.example.mapdemo.helper.FirestoreHelper.FL_TARGETID;
import static com.example.mapdemo.helper.FirestoreHelper.FL_TYPE;
import static com.example.mapdemo.helper.FirestoreHelper.FL_USERID;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.helper.CallbackHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class FirestoreDataManagerImpl implements FirestoreDataManager {
    FirebaseFirestore db;
    private List<ListenerRegistration> listenerRegistrations;
    @Inject
    public FirestoreDataManagerImpl(FirebaseFirestore db,List<ListenerRegistration> listenerRegistrations){
        this.db = db;
        this.listenerRegistrations = listenerRegistrations;
    }

    @Override
    public void getAccommodationById(String idAccom, CallbackHelper callback) {
        DocumentReference docRef = db.collection(CL_ACCOM).document(idAccom);
        ListenerRegistration registration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }
                if (value != null && value.exists()) {
                    AccommodationResponse accommodation = value.toObject(AccommodationResponse.class);
                    callback.onAccommodationResRecieved(accommodation);
                }
            }
        });
        listenerRegistrations.add(registration);
    }

    @Override
    public void addAccommodation(Accommodation accom) {
        Map<String, Object> user = new HashMap<>();
        user.put(FL_ACCOMID, accom.getAccommodationId());
        user.put(FL_NAME, accom.getName());
        user.put(FL_PRICE, accom.getPrice());
        user.put(FL_FREEROOM, accom.getFreeroom());
        user.put(FL_IMAGE, accom.getImage());
        user.put(FL_DESCRIPTION, accom.getDescription());
        user.put(FL_ADDRESS, accom.getAddress());
        user.put(FL_LONGTITUDE, accom.getLongitude());
        user.put(FL_LATITUDE, accom.getLatitude());
        user.put(FL_CITYID, accom.getCityId());
        db.collection(CL_ACCOM).document(accom.getAccommodationId()).set(user);
    }


    @Override
    public void getFavoriteByUserId(String idUser, CallbackHelper callback) {
       db.collection(CL_FAVORITE)
                .whereEqualTo(FL_USERID, idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Favorite> favorites= new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String idTarget = document.getData().get(FL_TARGETID).toString();
                                String idUser = document.getData().get(FL_USERID).toString();
                                String type = document.getData().get(FL_TYPE).toString();
                                Favorite favorite = new Favorite(document.getId(), idTarget, idUser, type);
                                favorites.add(favorite);
                            }
                            callback.onListFavoriteRecieved(favorites);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void addFavorite(Favorite favorite) {
        Map<String, Object> favoriteMap = new HashMap<>();
        favoriteMap.put(FL_TARGETID, favorite.getIdTarget());
        favoriteMap.put(FL_USERID, favorite.getIdUser());
        favoriteMap.put(FL_TYPE, favorite.getType());
        db.collection(CL_FAVORITE).document(favorite.getIdFavorite()).set(favoriteMap);
    }

    @Override
    public void deleteFavorite(String favoriteId) {
        db.collection(CL_FAVORITE).document(favoriteId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }
    public void removeAllListeners() {
        for (ListenerRegistration registration : listenerRegistrations) {
            registration.remove();
        }
        listenerRegistrations.clear();
    }
}