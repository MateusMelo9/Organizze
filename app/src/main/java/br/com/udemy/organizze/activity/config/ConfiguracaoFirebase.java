package br.com.udemy.organizze.activity.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static DatabaseReference dbReferece;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getDbReferece(){
        if(dbReferece != null){
            dbReferece = FirebaseDatabase.getInstance().getReference();
        }
        return dbReferece;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
