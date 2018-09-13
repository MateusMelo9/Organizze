package br.com.udemy.organizze.activity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.udemy.organizze.R;
import br.com.udemy.organizze.activity.config.ConfiguracaoFirebase;
import br.com.udemy.organizze.activity.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText et_nome,et_email,et_senha;
    private Button btn_cadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        et_nome = findViewById(R.id.et_nome);
        et_email = findViewById(R.id.et_email);
        et_senha = findViewById(R.id.et_senha);

        btn_cadastrar = findViewById(R.id.btn_cadastrar);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtNome = et_nome.getText().toString();
                String txtEmail = et_email.getText().toString();
                String txtSenha = et_senha.getText().toString();

                if(!txtNome.isEmpty()){
                    if(!txtEmail.isEmpty()){
                        if(!txtSenha.isEmpty()){
                            usuario = new Usuario();
                            usuario.setNome(txtNome);
                            usuario.setEmail(txtEmail);
                            usuario.setSenha(txtSenha);
                            Log.i("FIREBASE",usuario.getEmail());
                            cadastrarUsuario(usuario);
                        }else {
                            Toast.makeText(CadastroActivity.this,"Campo senha está vazio.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,"Campo email está vazio.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,"Campo nome está vazio.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cadastrarUsuario(Usuario usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                }else {
                    String erro ="";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Por favor, digite um e-mail válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "Esta conta já foi cadastradada";
                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuário: "+e.getMessage();
                        e.printStackTrace();
                    }

                    Log.i("AUTENTICACAO",task.getException().getMessage());
                    Toast.makeText(CadastroActivity.this,erro,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
