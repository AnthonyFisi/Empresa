package com.example.empresayego.Repository.Repositorio;

import com.example.empresayego.Repository.Modelo.Gson.GsonRepartidor_Bi;
import com.example.empresayego.Repository.Service.Repartidor_BiService;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repartidor_BiRepository {


    private static final String RESTAURANTE_PEDIDO_SERVICE_URL_BASE="https://backend-tiend-go.herokuapp.com/";

    private Repartidor_BiService mRepartidorBiService;
    private MutableLiveData<GsonRepartidor_Bi> gsonRepartidorMutableLiveData;

    public Repartidor_BiRepository(){
       gsonRepartidorMutableLiveData=new MutableLiveData<>();
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client= new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(RESTAURANTE_PEDIDO_SERVICE_URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRepartidorBiService = retrofit.create(Repartidor_BiService.class);
    }

    public void listaRepartidores(String token,int idrepartidor){
        mRepartidorBiService.listaRepartidores(token,idrepartidor).enqueue(new Callback<GsonRepartidor_Bi>() {
            @Override
            public void onResponse(Call<GsonRepartidor_Bi> call, Response<GsonRepartidor_Bi> response) {
                if(response.code()==200 && response.body()!=null){

                    gsonRepartidorMutableLiveData.setValue(response.body());
                }else {
                    gsonRepartidorMutableLiveData.postValue(null);

                }
            }

            @Override
            public void onFailure(Call<GsonRepartidor_Bi> call, Throwable t) {
                gsonRepartidorMutableLiveData.postValue(null);

            }
        });
    }


    public LiveData<GsonRepartidor_Bi> getGsonRepartidorDataLiveData(){
        return gsonRepartidorMutableLiveData;
    }
}