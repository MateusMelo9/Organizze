package br.com.udemy.organizze.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.udemy.organizze.R;
import br.com.udemy.organizze.activity.config.ConfiguracaoFirebase;
import br.com.udemy.organizze.activity.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText et_senha,et_email;
    private Button btn_entrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_senha = findViewById(R.id.et_senha);

        btn_entrar=findViewById(R.id.btn_entrar);

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = et_email.getText().toString();
                String txtSenha = et_senha.getText().toString();

                    if(!txtEmail.isEmpty()){
                        if(!txtSenha.isEmpty()){
                            usuario = new Usuario();
                            usuario.setEmail(txtEmail);
                            usuario.setSenha(txtSenha);
                            validarLogin();
                        }else {
                            Toast.makeText(LoginActivity.this,"Campo senha está vazio.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this,"Campo email está vazio.",Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                }else{
                    String erro = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        erro = "Esse e-mail não existe!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail e Senha não corresponde a usuário cadastrado.";
                    }catch (Exception e){
                        erro = "Erro ao fazer login: "+e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,erro,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal(){
        Intent i = new Intent(LoginActivity.this,PrincipalActivity.class);
        startActivity(i);
        finish();
    }
}
