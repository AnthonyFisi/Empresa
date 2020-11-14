package com.example.empresayego.Login.Service;

import com.example.empresayego.Login.Modelo.Empresa_bi;
import com.example.empresayego.Repository.Modelo.Empresa;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface Empresa_biService {


    @GET("/EmpresaBi2Controller/findBy/{idusuario}")
    Call<Empresa>  getEmpresaBiById(@Header("Authorization")String auth, @Path("idusuario") int idusuario);


}
