package project.roy.socialmedia.ui.account;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.renderscript.ScriptGroup;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.TimeLineAdapter;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.Media;
import project.roy.socialmedia.data.model.Timeline;
import project.roy.socialmedia.data.model.User;
import project.roy.socialmedia.presenter.AccountPresenter;
import project.roy.socialmedia.presenter.TimelinePresenter;
import project.roy.socialmedia.ui.home.HomeActivity;
import project.roy.socialmedia.ui.timeline.DetailsTimelineActivity;
import project.roy.socialmedia.ui.timeline.TimelineView;
import project.roy.socialmedia.util.Constant;
import project.roy.socialmedia.util.ImageUtil;
import project.roy.socialmedia.util.ShowAlert;

import static android.view.View.GONE;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener, AccountView, TimelineView, TimeLineAdapter.OnDetailListener {

    private Toolbar toolbar;
    private CircleImageView imgAvatar;
    private ImageView imgCamera;
    private TextView txtNama, txtEditPass, txtUsername,edtUsername, txtEditChildrenAge;
    private ImageView edtNama, imageView;
    private Dialog dialogEdtPass;
    private Dialog dialogEdtName;
    private Dialog dialogEdtChildrenAge;
    private LovelyProgressDialog waitingDialog;
    private static final int PICK_IMAGE = 1994;
    private AccountPresenter accountPresenter;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar, postsProgressBar;
    private SwipeRefreshLayout swipeContainer;
    private TimelinePresenter timelinePresenter;
    private TimeLineAdapter timelineAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        toolbar = findViewById(R.id.toolbar);
        imgAvatar = findViewById(R.id.foto_profil);
        imgCamera = findViewById(R.id.pick_photo);
        txtNama = findViewById(R.id.txt_nama);
        txtUsername = findViewById(R.id.txt_username);
        edtNama = findViewById(R.id.save_name);
        edtUsername = findViewById(R.id.save_username);
        txtEditPass = findViewById(R.id.txt_edit_password);
        txtEditChildrenAge = findViewById(R.id.txt_edit_children_age);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        dialogEdtPass = new Dialog(this);
        dialogEdtName = new Dialog(this);
        dialogEdtChildrenAge = new Dialog(this);

        progressBar = findViewById(R.id.progressBar);
        imageView =  findViewById(R.id.imageView);
        postsProgressBar = findViewById(R.id.postsProgressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                timelinePresenter.getTimelineById(SaveUserData.getInstance().getUser().getId());
                swipeContainer.setRefreshing(false);
            }
        });

        waitingDialog = new LovelyProgressDialog(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Akun");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        accountPresenter = new AccountPresenter(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 0);
        }
        initView();

        imgCamera.setOnClickListener(this);
        edtNama.setOnClickListener(this);
        edtUsername.setOnClickListener(this);
        txtEditPass.setOnClickListener(this);
        txtEditChildrenAge.setOnClickListener(this);
    }

    private void initView() {
        timelinePresenter = new TimelinePresenter(this);
        timelinePresenter.getTimeline();
        timelineAdapter = new TimeLineAdapter(this);
        timelineAdapter.setOnClickDetailListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(timelineAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        txtNama.setText(SaveUserData.getInstance().getUser().getName());
        txtUsername.setText(SaveUserData.getInstance().getUser().getUsername());

        if (SaveUserData.getInstance().getImagePath() != null){
            File imgFile = new  File(SaveUserData.getInstance().getImagePath());

            if(imgFile.exists()){
                Bitmap avatar = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgAvatar.setImageBitmap(avatar);
            }
            else {
                ShowAlert.showSnackBar(coordinatorLayout, "File foto tidak ditemukan");
            }
        }
        else if(SaveUserData.getInstance().getUser().getPicture() != null){

            if (!SaveUserData.getInstance().getUser().getPicture().toString().isEmpty()) {
                Picasso.get().load(Constant.BASE_URL + SaveUserData.getInstance().getUser().getPicture().toString())
                        .into(imgAvatar);
            }
        }
        else {
            ShowAlert.showSnackBar(coordinatorLayout, "Belum tersedia foto profil");
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_edit_password){
            TextView txtclose;
            Button btnSubmit;
            EditText etOldPass,etNewPass;
            ImageView oldVisibility,newVisibility,oldInvisibility,newInvisibility;

            dialogEdtPass.setContentView(R.layout.dialog_edit_password);
            txtclose = dialogEdtPass.findViewById(R.id.txtclose);
            btnSubmit = dialogEdtPass.findViewById(R.id.btn_submit_password);
            etOldPass = dialogEdtPass.findViewById(R.id.old_password);
            etNewPass = dialogEdtPass.findViewById(R.id.new_password);
            oldVisibility = dialogEdtPass.findViewById(R.id.img_visibility_old);
            newVisibility = dialogEdtPass.findViewById(R.id.img_visibility_new);
            oldInvisibility = dialogEdtPass.findViewById(R.id.img_invisibility_old);
            newInvisibility = dialogEdtPass.findViewById(R.id.img_invisibility_new);

            txtclose.setOnClickListener(v1 -> dialogEdtPass.dismiss());
            oldVisibility.setOnClickListener(v12 -> {
                etOldPass.setInputType(InputType.TYPE_CLASS_TEXT);
                oldVisibility.setVisibility(GONE);
                oldInvisibility.setVisibility(View.VISIBLE);
            });
            oldInvisibility.setOnClickListener(v13 -> {
                etOldPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                oldInvisibility.setVisibility(GONE);
                oldVisibility.setVisibility(View.VISIBLE);
            });
            newVisibility.setOnClickListener(v14 -> {
                etNewPass.setInputType(InputType.TYPE_CLASS_TEXT);
                newVisibility.setVisibility(GONE);
                newInvisibility.setVisibility(View.VISIBLE);
            });
            newInvisibility.setOnClickListener(v15 -> {
                etNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                newVisibility.setVisibility(View.VISIBLE);
                newInvisibility.setVisibility(GONE);
            });
            btnSubmit.setOnClickListener(v19 -> {
                String oldPassword = etOldPass.getText().toString().trim();
                String newPassword = etNewPass.getText().toString().trim();
                if(oldPassword.isEmpty()){
                    etOldPass.setError(getResources().getString(R.string.text_old_pass_empty));
                    etOldPass.requestFocus();
                }
                else if (newPassword.isEmpty()){
                    etNewPass.setError(getResources().getString(R.string.text_new_pass_empty));
                    etNewPass.requestFocus();
                }
                else {
                    accountPresenter.changePassword(AccountActivity.this,newPassword,oldPassword);
                }
            });

            dialogEdtPass.show();

        }

        if(v.getId() == R.id.txt_edit_children_age){
            EditText etPassword;
            Spinner spnChildrenAge;
            Button btnSubmit;
            TextView txtclose;
            final String[] childrenAgeSelected = new String[1];

            dialogEdtChildrenAge.setContentView(R.layout.dialog_edit_children_age);
            btnSubmit = dialogEdtChildrenAge.findViewById(R.id.btn_submit_name);
            spnChildrenAge = dialogEdtChildrenAge.findViewById(R.id.spn_children_age);
            etPassword = dialogEdtChildrenAge.findViewById(R.id.password);
            txtclose = dialogEdtChildrenAge.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(v1 -> dialogEdtChildrenAge.dismiss());
            final List<String> list = new ArrayList<String>();
            list.add("Usia Anak Anda");
            list.add("< 1 Tahun");
            list.add("1 Tahun");
            list.add("2 Tahun");
            list.add("3 Tahun");
            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                    R.layout.spinner_item, list);
            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnChildrenAge.setAdapter(adp1);
            if (SaveUserData.getInstance().getChildrenAge() != null) {
                int spinnerPosition = adp1.getPosition(SaveUserData.getInstance().getChildrenAge());
                spnChildrenAge.setSelection(spinnerPosition);
            }
            spnChildrenAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                    // TODO Auto-generated method stub
                    childrenAgeSelected[0] = list.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            btnSubmit.setOnClickListener(v18 -> {
                String password = etPassword.getText().toString().trim();

                if (password.isEmpty()) {
                    etPassword.setError(getResources().getString(R.string.text_password_empty));
                    etPassword.requestFocus();
                } else if(childrenAgeSelected[0].isEmpty() && childrenAgeSelected.equals("Usia Anak Anda")){
                    ShowAlert.showToast(getApplicationContext(), "Anda belum memilih usia anak anda");
                }else {
                    accountPresenter.changeProfileChildrenAge(AccountActivity.this, SaveUserData.getInstance().getUser().getName(), SaveUserData.getInstance().getUser().getUsername(), password, childrenAgeSelected[0]);
                }
            });
            dialogEdtChildrenAge.show();

        }
        if (v.getId() == R.id.save_name){
            TextView txtclose;
            Button btnSubmit;
            EditText etNewName, etPassword;
            ImageView passVisibility, passInvisibility;

            dialogEdtName.setContentView(R.layout.dialog_edit_name);
            txtclose = dialogEdtName.findViewById(R.id.txtclose);
            btnSubmit = dialogEdtName.findViewById(R.id.btn_submit_name);
            etNewName = dialogEdtName.findViewById(R.id.new_name);
            etPassword = dialogEdtName.findViewById(R.id.password);
            passVisibility = dialogEdtName.findViewById(R.id.img_visibility_pass);
            passInvisibility = dialogEdtName.findViewById(R.id.img_invisibility_pass);

            txtclose.setOnClickListener(v1 -> dialogEdtName.dismiss());
            passVisibility.setOnClickListener(v17 -> {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                passVisibility.setVisibility(View.GONE);
                passInvisibility.setVisibility(View.VISIBLE);
            });
            passInvisibility.setOnClickListener(v16 -> {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passVisibility.setVisibility(View.VISIBLE);
                passInvisibility.setVisibility(View.GONE);
            });
            btnSubmit.setOnClickListener(v18 -> {
                String newName = etNewName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if(newName.isEmpty()){
                    etNewName.setError(getResources().getString(R.string.text_name_empty));
                    etNewName.requestFocus();
                }
                else if (password.isEmpty()){
                    etPassword.setError(getResources().getString(R.string.text_password_empty));
                    etPassword.requestFocus();
                }
                else {
                    accountPresenter.changeProfile(AccountActivity.this,newName,SaveUserData.getInstance().getUser().getUsername(),password);
                }
            });

            dialogEdtName.show();
        }

        if (v.getId() == R.id.save_username){
            TextView txtclose, txtHeaderNewUsername, txtViewNewUsername;
            Button btnSubmit;
            EditText etNewUsername, etPassword;
            ImageView passVisibility, passInvisibility;

            dialogEdtName.setContentView(R.layout.dialog_edit_name);
            txtclose = dialogEdtName.findViewById(R.id.txtclose);
            btnSubmit = dialogEdtName.findViewById(R.id.btn_submit_name);
            etNewUsername = dialogEdtName.findViewById(R.id.new_name);
            etPassword = dialogEdtName.findViewById(R.id.password);
            passVisibility = dialogEdtName.findViewById(R.id.img_visibility_pass);
            passInvisibility = dialogEdtName.findViewById(R.id.img_invisibility_pass);
            txtHeaderNewUsername = dialogEdtName.findViewById(R.id.header_change_name);
            txtViewNewUsername = dialogEdtName.findViewById(R.id.view_new_name);

            etNewUsername.setHint(R.string.hint_new_username);
            txtHeaderNewUsername.setText("Ubah Username");
            txtViewNewUsername.setText(R.string.hint_new_username);
            txtclose.setOnClickListener(v1 -> dialogEdtName.dismiss());
            passVisibility.setOnClickListener(v17 -> {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                passVisibility.setVisibility(View.GONE);
                passInvisibility.setVisibility(View.VISIBLE);
            });
            passInvisibility.setOnClickListener(v16 -> {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passVisibility.setVisibility(View.VISIBLE);
                passInvisibility.setVisibility(View.GONE);
            });
            btnSubmit.setOnClickListener(v18 -> {
                String newUsername = etNewUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if(newUsername.isEmpty()){
                    etNewUsername.setError(getResources().getString(R.string.text_name_empty));
                    etNewUsername.requestFocus();
                }
                else if (password.isEmpty()){
                    etPassword.setError(getResources().getString(R.string.text_password_empty));
                    etPassword.requestFocus();
                }
                else {
                    accountPresenter.changeProfile(AccountActivity.this,SaveUserData.getInstance().getUser().getName(),newUsername,password);
                }
            });

            dialogEdtName.show();
        }

        if (v.getId() == R.id.pick_photo){
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( AccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 0);
            }
            pickPhoto();

        }
    }

    public void pickPhoto(){
        new AlertDialog.Builder(this)
                .setTitle("Foto Profil")
                .setMessage("Apakah anda yakin ingin mengupdate foto profil?")
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                    dialogInterface.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK ) {
            if (data == null) {
                Toast.makeText(this, "Tidak ada file image dipilih", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                    imgBitmap = ImageUtil.cropToSquare(decoded);
                    InputStream is = ImageUtil.convertBitmapToInputStream(imgBitmap);
                    final Bitmap liteImage = ImageUtil.makeImageLite(is,
                            imgBitmap.getWidth(), imgBitmap.getHeight(),
                            ImageUtil.AVATAR_WIDTH, ImageUtil.AVATAR_HEIGHT);

                    //String imageBase64 = ImageUtil.encodeBase64(liteImage);
                    String imageBase64 = ImageUtil.encodeBase64(liteImage);

                    Uri tempUri = ImageUtil.getImageUri(AccountActivity.this, imgBitmap);

                    File finalFile = new File(ImageUtil.getRealPathFromURI(AccountActivity.this, tempUri));

                    System.out.println("url temp : " + tempUri);;
                    System.out.println("final file : " + finalFile);

                    waitingDialog.setCancelable(false)
                            .setTitle("Avatar updating....")
                            .setTopColorRes(R.color.colorPrimary)
                            .show();
                    accountPresenter.updatePhotoProfile(AccountActivity.this,SaveUserData.getInstance().getUser().getId(),finalFile,liteImage);
                    //new RetrieveImage(EditProfilActivity.this,avatar);

                    System.out.println("url : " + String.valueOf(finalFile));
                    //Bitmap b = fileCache.getFile(url);
//                    Bitmap b = fileCache.decodeFile(f);
//                        avatar.setImageDrawable(ImageUtil.roundedImage(this,liteImage));
                    waitingDialog.dismiss();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            setResult(RESULT_OK);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessage(String s) {
        ShowAlert.showToast(AccountActivity.this,s);
    }

    @Override
    public void showMessageSnackbar(String message) {
        dialogEdtPass.dismiss();
        dialogEdtName.dismiss();
        dialogEdtChildrenAge.dismiss();
        ShowAlert.showSnackBar(coordinatorLayout, message);
    }

    @Override
    public void showChange(String username, String name) {
        txtNama.setText(name);
        txtUsername.setText(username);
    }

    @Override
    public void showAvatarChange(Bitmap photo) {
        imgAvatar.setImageDrawable(ImageUtil.roundedImage(AccountActivity.this,photo));
    }

    @Override
    public void onSuccessShowTimeline(List<Timeline> timelineList) {
       // progressBar.setVisibility(View.GONE);
        timelineAdapter.setData(timelineList);
    }

    @Override
    public void onFailureShowTimeline(String messages) {
        ShowAlert.showToast(this,messages);
    }

    @Override
    public void onSuccessPostTimeline(String messages) {

    }

    @Override
    public void onFailurePostTimeline(String messages) {

    }

    @Override
    public void onSuccessDeleteTimeline(String message) {

    }

    @Override
    public void onFailedDeleteTimeline(String message) {

    }

    @Override
    public void onItemClick(Timeline timeline, List<Media> mediaList, User user) {
        Intent intent = new Intent(this, DetailsTimelineActivity.class);
        intent.putExtra("timeline", timeline);
        intent.putParcelableArrayListExtra("media", (ArrayList<? extends Parcelable>) mediaList);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onAuthorClick(int idUser) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("idUser", idUser);
        startActivity(intent);
    }

    @Override
    public void onDeleteTimeline(int timelineId) {

    }

    @Override
    public void onNameSelected(User user) {

    }
}
