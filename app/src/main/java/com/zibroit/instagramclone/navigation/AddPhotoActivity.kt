package com.zibroit.instagramclone.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.zibroit.instagramclone.R
import com.zibroit.instagramclone.navigation.model.ContentDTO
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null
    var fireStore : FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        /*storage 초기화*/
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()

        /*앨범 열기*/
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent,PICK_IMAGE_FROM_ALBUM)

        /*이미지 추가 이벤트*/
        addphoto_btn_upload.setOnClickListener{
            contentUpload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_FROM_ALBUM){
            if(resultCode== Activity.RESULT_OK){
                /*사진을 선택했을때 이미지 경로가 여기로 넘어옴*/
                photoUri = data?.data
                addphoto_image.setImageURI(photoUri)
            }else{
                /*취소했을때 작동하는 부분*/
                finish()
            }
        }
    }
    fun contentUpload(){
        /*파일명 만들기*/
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_"+timestamp+"_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        /*파일 업로드*/
        /*1.promise 방식*/
        storageRef?.putFile(photoUri!!)?.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            var contentDTO = ContentDTO()

            /*이미지의 url 넣기*/
            contentDTO.imageUrl = uri.toString()

            /*유저의 uid 넣기*/
            contentDTO.uid = auth?.currentUser?.uid

            /*유저의 userId 넣기*/
            contentDTO.userId = auth?.currentUser?.email

            /*유저의 explain of content 넣기*/
            contentDTO.explain = addphoto_edit_explain.text.toString()

            /*timestamp 넣기*/
            contentDTO.timestamp = System.currentTimeMillis()

            fireStore?.collection("images")?.document()?.set(contentDTO)

            setResult(Activity.RESULT_OK)

            finish()
        }
        /*2.콜백 방식*/
        /*storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri->
                var contentDTO = ContentDTO()

                *//*이미지의 url 넣기*//*
                contentDTO.imageUrl = uri.toString()

                *//*유저의 uid 넣기*//*
                contentDTO.uid = auth?.currentUser?.uid

                *//*유저의 userId 넣기*//*
                contentDTO.userId = auth?.currentUser?.email

                *//*유저의 explain of content 넣기*//*
                contentDTO.explain = addphoto_edit_explain.text.toString()

                *//*timestamp 넣기*//*
                contentDTO.timeStamp = System.currentTimeMillis()

                fireStore?.collection("images")?.document()?.set(contentDTO)

                setResult(Activity.RESULT_OK)

                finish()
            }
        }*/
    }
}