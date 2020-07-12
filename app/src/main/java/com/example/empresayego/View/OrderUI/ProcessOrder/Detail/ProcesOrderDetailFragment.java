package com.example.empresayego.View.OrderUI.ProcessOrder.Detail;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.empresayego.R;
import com.example.empresayego.Repository.Modelo.Orden_estado_restaurante;
import com.example.empresayego.Repository.Modelo.Orden_estado_restaurantePK;
import com.example.empresayego.Repository.Modelo.ProductoJOINregistroPedidoJOINpedido;
import com.example.empresayego.Repository.Modelo.Restaurante_Pedido;
import com.example.empresayego.View.OrderUI.NewOrder.Detail.ProductosResultsAdapter;
import com.example.empresayego.ViewModel.Orden_estado_restauranteViewModel;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProcesOrderDetailFragment extends Fragment {

    private Orden_estado_restauranteViewModel viewModel;

    private boolean answerUpdateState=false;

    private OnDataPass dataPasser;

    private Button fragment_proces_order_detail_PEDIDO_LISTO;
    private TextView fragment_proces_order_detail_NUMERO_ORDEN,fragment_proces_order_detail_HORA_LLEGADA,fragment_proces_order_detail_NOMBRE_CLIENTE,
            fragment_proces_order_detail_TIPO_ENVIO,fragment_proces_order_detail_ESTADO_PAGO,fragment_proces_order_detail_COSOTO_TOTAL,fragment_proces_order_detail_NUMERO_TELEFONO;


    private RecyclerView fragment_proces_order_detail_RECYCLER_VIEW_PRODUCTOS;

    private Restaurante_Pedido mRestaurante_pedido;

    private List<ProductoJOINregistroPedidoJOINpedido> listaProducto ;

    private int cantidadTiempo;

    private ProductosResultsAdapter adapter;

    public ProcesOrderDetailFragment() {
        adapter= new ProductosResultsAdapter();
        cantidadTiempo = 5;
        listaProducto = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(Orden_estado_restauranteViewModel.class);
        viewModel.init();
        viewModel.getOrden_estado_restauranteLiveData().observe(this, new Observer<Orden_estado_restaurante>() {
            @Override
            public void onChanged(Orden_estado_restaurante orden_estado_restaurante) {

                if(orden_estado_restaurante !=null){
                    answerUpdateState=true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_proces_orders_detail, container, false);


        //DECLARAR WIDGETS
        declararWidgets(view);
        //CLICK BUTTON

        return view;
    }

    private void declararWidgets(View view){




        fragment_proces_order_detail_NUMERO_ORDEN=view.findViewById(R.id.fragment_proces_order_detail_NUMERO_ORDEN);
        fragment_proces_order_detail_HORA_LLEGADA=view.findViewById(R.id.fragment_proces_order_detail_HORA_LLEGADA);
        fragment_proces_order_detail_NOMBRE_CLIENTE=view.findViewById(R.id.fragment_proces_order_detail_NOMBRE_CLIENTE);
        fragment_proces_order_detail_TIPO_ENVIO=view.findViewById(R.id.fragment_proces_order_detail_TIPO_ENVIO);
        fragment_proces_order_detail_ESTADO_PAGO=view.findViewById(R.id.fragment_proces_order_detail_ESTADO_PAGO);
        fragment_proces_order_detail_COSOTO_TOTAL=view.findViewById(R.id. fragment_proces_order_detail_COSOTO_TOTAL);
        fragment_proces_order_detail_PEDIDO_LISTO=view.findViewById(R.id.fragment_proces_order_detail_PEDIDO_LISTO);
        fragment_proces_order_detail_RECYCLER_VIEW_PRODUCTOS=view.findViewById(R.id.fragment_proces_order_detail_RECYCLER_VIEW_PRODUCTOS);
        fragment_proces_order_detail_NUMERO_TELEFONO=view.findViewById(R.id.fragment_proces_order_detail_NUMERO_TELEFONO);

    }

    private void setDataWidget(){


        String numeroOrden="#"+mRestaurante_pedido.getIdempresa()+""+
                mRestaurante_pedido.getIdpedido()+""+
                mRestaurante_pedido.getIdventa();
        fragment_proces_order_detail_NUMERO_ORDEN.setText(numeroOrden);

        String pattern = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String fecha=dateFormat.format(mRestaurante_pedido.getVenta_fecha());

        fragment_proces_order_detail_HORA_LLEGADA.setText(fecha);

        fragment_proces_order_detail_NOMBRE_CLIENTE.setText(mRestaurante_pedido.getUsuario_nombre());

        fragment_proces_order_detail_TIPO_ENVIO.setText(mRestaurante_pedido.getNombre_tipo_envio());

        fragment_proces_order_detail_ESTADO_PAGO.setText(mRestaurante_pedido.getTipo_estado());

        fragment_proces_order_detail_COSOTO_TOTAL.setText(String.valueOf(mRestaurante_pedido.getVenta_costototal()));


        adapter.setResults(listaProducto);
        fragment_proces_order_detail_RECYCLER_VIEW_PRODUCTOS.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragment_proces_order_detail_RECYCLER_VIEW_PRODUCTOS.setAdapter(adapter);

        fragment_proces_order_detail_NUMERO_TELEFONO.setText(mRestaurante_pedido.getUsuario_celular());


    }





    private void updateStateOrden(Restaurante_Pedido mRestaurante_Pedido){

        fragment_proces_order_detail_PEDIDO_LISTO.setOnClickListener( v->{


            Timestamp time=new Timestamp(System.currentTimeMillis());

            Orden_estado_restaurantePK pk=new Orden_estado_restaurantePK();
            pk.setIdventa(mRestaurante_Pedido.getIdventa());
            pk.setIdestado_venta(3);

            Orden_estado_restaurante estado= new Orden_estado_restaurante();
            estado.setId(pk);
            estado.setDetalle("");
            estado.setFecha(null);

            viewModel.updateEstadoProces(estado,mRestaurante_Pedido.getIdusuario());
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                if(answerUpdateState){

                    passData(true);
                    getActivity().finish();
                }else {
                    Toast.makeText(getContext(),"NO FUE ACTUALIZADO",Toast.LENGTH_SHORT).show();
                }
            }, 3000);

        });
    }



    public void setPassData(Restaurante_Pedido restaurante_pedido){
        this.mRestaurante_pedido=restaurante_pedido;
        listaProducto.addAll(restaurante_pedido.getListaProductos());
        setDataWidget();
        System.out.println("DI DE LA VENTA   " + restaurante_pedido.getIdventa() );
        updateStateOrden(restaurante_pedido);
    }

    public void passData(boolean agregar) {
        dataPasser.onDataPass(agregar);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        dataPasser = (ProcesOrderDetailFragment.OnDataPass) context;
    }


    public interface OnDataPass {
        void onDataPass(boolean agregar);
    }

}