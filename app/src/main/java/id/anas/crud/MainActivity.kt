package id.anas.crud

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import id.anas.crud.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //Membuat Kode Permintaan
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Inisialisasi ID (Button)
        logout.setOnClickListener(this)
        save.setOnClickListener(this)
        showdata.setOnClickListener(this)
        //Mendapatkan Instance Firebase Autentifikasi
        auth = FirebaseAuth.getInstance()
    }

    // Mengecek apakah ada data kosong, digunakan pada Tutorial Selanjutnya
    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.save -> {
                // Statement program untuk simpan data
                //Mendapatkan UserID dari pengguna yang Terautentikasi
                val getUserID = auth!!.currentUser!!.uid

                //Mendapatkan Instance dari Database
                val database = FirebaseDatabase.getInstance()

                //Menyimpan Data yang diinputkan User kedalam Variable
                val getNIM: String = nim.getText().toString()
                val getNama: String = nama.getText().toString()
                val getJurusan: String = jurusan.getText().toString()

                // Mendapatkan Referensi dari Database
                val getReference: DatabaseReference
                getReference = database.reference

                // Mengecek apakah ada data yang kosong
                if (isEmpty(getNIM) || isEmpty(getNama) || isEmpty(getJurusan)) {
                    //Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
                    Toast.makeText(this@MainActivity, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                } else {
                    /* Jika Tidak, maka data dapat diproses dan meyimpannya pada Database        Menyimpan data referensi pada Database berdasarkan User ID dari masing-masing Akun
                    */
                    getReference.child("Admin").child(getUserID).child("Mahasiswa").push()
                        .setValue(data_mahasiswa(getNIM, getNama, getJurusan))
                        .addOnCompleteListener(this) { //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                            nim.setText("")
                            nama.setText("")
                            jurusan.setText("")
                            Toast.makeText(this@MainActivity, "Data Tersimpan", Toast.LENGTH_SHORT).show()
                        }
                }
            }

        }
            }
            R.id.logout ->
                // Statement program untuk logout/keluar
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            Toast.makeText(this@MainActivity, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            R.id.showdata -> {
            }
        }
    }
}

