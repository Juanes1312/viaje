package com.example.viaje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText jetcodigo,jetdestino,jetcantidadpersonas,jetvalor;
    TextView jtvactivo;
    Button jbtguardar,jbtconsultar,jbtanular,jbtcancelar;
    long resp;
    int sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        jetcodigo=findViewById(R.id.etcodigo);
        jetdestino=findViewById(R.id.etciudad);
        jetcantidadpersonas=findViewById(R.id.etcantidadpersonas);
        jetvalor=findViewById(R.id.etvalor);
        jtvactivo=findViewById(R.id.tvactivo);
        jbtguardar=findViewById(R.id.btguardar);
        jbtconsultar=findViewById(R.id.btconsultar);
        jbtanular=findViewById(R.id.btanular);
        jbtcancelar=findViewById(R.id.btcancelar);
        sw=0;
    }

    public void limpiar_campos(){
        sw=0;
        jetcodigo.setText("");
        jetdestino.setText("");
        jetcantidadpersonas.setText("");
        jetvalor.setText("");
        jetcodigo.requestFocus();
    }

    public void Guardar(View view){
        String codigo,destino,cantidadpersonas,valor;
        codigo=jetcodigo.getText().toString();
        destino=jetdestino.getText().toString();
        cantidadpersonas=jetcantidadpersonas.getText().toString();
        valor=jetvalor.getText().toString();
        if (codigo.isEmpty() || destino.isEmpty() || cantidadpersonas.isEmpty()
                || valor.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            Conexion_viaje admin=new Conexion_viaje(this,"viaje.bd",null,1);
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues dato=new ContentValues();
            dato.put("CodigoCliente",codigo);
            dato.put("ciudad",destino);
            dato.put("cantidad",cantidadpersonas);
            dato.put("valor",valor);
            if (sw == 0)
                resp=db.insert("TblViaje",null,dato);
            else{
                sw=0;
                resp=db.update("TblViaje",dato,"CodigoCliente='" + codigo + "'",null);
            }
            if (resp > 0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                limpiar_campos();
            }
            else{
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    public void Consultar(View view){
        Consultar_Cliente();
    }

    public void Consultar_Cliente(){
        String codigo;
        codigo=jetcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this, "Codigo del viaje requerido", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else {
            Conexion_viaje admin = new Conexion_viaje(this, "viaje.bd", null, 1);
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblViaje where CodigoCliente='" + codigo + "'",null);
            if (fila.moveToNext()){
                sw=1;
                jetdestino.setText(fila.getString(1));
                jetcantidadpersonas.setText(fila.getString(2));
                jetvalor.setText(fila.getString(3));
                Toast.makeText(this, "Registrado encontrado", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Registro no existe", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }
    public void Cancelar(View view){
        limpiar_campos();
    }

}
