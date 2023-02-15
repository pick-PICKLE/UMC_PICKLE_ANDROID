package com.example.myapplication.ui.main.profile.myprofile

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMyprofileBinding
import com.example.myapplication.db.remote.model.UserProfileEditDto
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyprofileFragment() : BaseFragment<FragmentMyprofileBinding>(R.layout.fragment_myprofile) {

    private lateinit var userViewModel: UserViewModel
    private var defaultImage = R.drawable.icon_myprofile_profileimage
    private var new_image_uri: Uri? = null

    override fun init() {
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        // 이미지를 넣을 경우
        userViewModel.profile_photo.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer<Uri> {
                Glide.with(this)
                    .load(it) //이미지
                    .into(binding.myprofileImagePhoto)
                //보여줄 위치
                new_image_uri = it
            })

        // 이미지를 삭제했을 경우
        userViewModel.default_profile_photo.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer<Int> {
                Glide.with(this)
                    .load(it)
                    .into(binding.myprofileImagePhoto)
            })

        hideBottomNavigation(true)
        initAppbar(binding.myprofileToolbar, "내 정보 수정", true, false)

        // 자신의 프로필 사진을 눌렀을 경우
        binding.myprofileImagePhoto.setOnClickListener {
//            openDialog(requireContext())
            val bottomSheetDialogFragment: BottomSheetDialogFragment = PermissionFragment()
            bottomSheetDialogFragment.show(parentFragmentManager, null)
        }

        // 저장 버튼을 눌렀을 경우
        binding.myprofileTextviewSavebutton.setOnClickListener {
            if (binding.myprofileTextviewEmail.text!!.length == 0) {
                Toast.makeText(requireContext(), "이메일을 제대로 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.myprofileTextviewName.text!!.length == 0) {
                Toast.makeText(requireContext(), "이름을 제대로 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.set_user_profile_data(
                    UserProfileEditDto(
                        binding.myprofileTextviewEmail.text.toString(),
                        new_image_uri.toString(),
                        binding.myprofileTextviewName.text.toString()
                    )
                )
                Toast.makeText(requireContext(), "프로필 정보가 편집되었습니다.", Toast.LENGTH_SHORT).show()
                new_image_uri = null
            }
        }

        // 수정 후 프로필 정보 재요청
        userViewModel.user_profile_edit_result_data.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                userViewModel.get_user_profile_data()
            })

        userViewModel.user_profile_data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                binding.myprofileTextviewName.setText(it.data!!.name)
                binding.myprofileTextviewEmail.setText(it.data!!.email)
                Glide.with(this@MyprofileFragment)
                    .load(it.data!!.image)
                    .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .into(binding.myprofileImagePhoto)
            } else {
                binding.myprofileTextviewName.setText("")
                binding.myprofileTextviewEmail.setText("")
                Glide.with(this)
                    .load(defaultImage)
                    .into(binding.myprofileImagePhoto)
            }
        })
    }

//    private fun openDialog(context: Context) {
//        val dialogLayout = layoutInflater.inflate(R.layout.dialog, null)
//        val dialogBuild = AlertDialog.Builder(context).apply {
//            setView(dialogLayout)
//        }
//        val dialog = dialogBuild.create().apply { show() }
//
//        val cameraAddBtn = dialogLayout.findViewById<Button>(R.id.dialog_btn_camera)
//        val fileAddBtn = dialogLayout.findViewById<AppCompatButton>(R.id.dialog_btn_file)
//
//        // 카메라 버튼 클릭 시
//        cameraAddBtn.setOnClickListener {
//            if (checkPermission(arrayOf(CAMERA))) { // 카메라 실행
//                camera()
////                // 2. TakePicturePreview
////                getTakePicturePreview.launch(null)    // Bitmap get
//            } else { // 권한 요청
//                requestMultiplePermission.launch(arrayOf(CAMERA))
//            }
//            dialog.dismiss()
//        }
//
//        // 갤러리 버튼 클릭 시
//        fileAddBtn.setOnClickListener {
//            if (checkPermission(arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE))) { // 갤러리 실행
//                gallery()
//            } else { // 권한 요청
//                requestMultiplePermission.launch(
//                    arrayOf(
//                        WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE
//                    )
//                )
//            }
//            dialog.dismiss()
//        }
//
//    }
//
//    // 권한 체크
//    fun checkPermission(permissions: Array<String>): Boolean {
//        var result: Boolean = false
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // api 23 부터
//            for (permission in permissions) {
//                if (ContextCompat.checkSelfPermission(
//                        requireContext(),
//                        permission
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) { // 권한 허용o
//                    result = true
//                }
//                // 권한 허용x -> 요청
//            }
//        }
//        return result
//    }
//
//    // 권한 요청
//    private val requestMultiplePermission =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
//            results.forEach {
//                if (!it.value) {
//                    showToast("권한 허용 필요")
//                } else {
//                    if (it.key == CAMERA) {
//                        CAMERA_PERMISSION = true // 카메라 권한 허용
//                    } else if (it.key == WRITE_EXTERNAL_STORAGE) {
//                        WRITE_EXTERNAL_STORAGE_PERMISSION = true // 갤러리 쓰기 허용
//                    } else if (it.key == READ_EXTERNAL_STORAGE) {
//                        READ_EXTERNAL_STORAGE_PERMISSION = true // 갤러리 읽기 허용
//                    }
//                }
//
//                if (CAMERA_PERMISSION) { // 권한 허용이후에 바로 실행하기 위함, 권한 체크는 한개씩 하기 때문에 이렇게 설정
//                    camera() // 카메라 실행
//                } else if (WRITE_EXTERNAL_STORAGE_PERMISSION && READ_EXTERNAL_STORAGE_PERMISSION) {
//                    gallery()  // 갤러리 실행
//                }
//
//            }
//        }
//
//    // 갤러리에서 받아온 이미지를 프로필 사진으로 설정
//    private val getContentImage =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) {
//            //결과 코드 OK , 결가값 null 아니면
//            if (it.resultCode == RESULT_OK && it.data != null) {
//                //값 담기
//                new_image_uri = it.data!!.data!!
//                //화면에 보여주기
//                Glide.with(this)
//                    .load(new_image_uri!!.path) //이미지
//                    .into(binding.myprofileImagePhoto) //보여줄 위치
//            }
//        }
//
//    // 갤러리 실행
//    private fun gallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.setDataAndType(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            "image/*"
//        )
//        getContentImage.launch(intent)
//    }
//
//    // 카메라를 실행한 후 찍은 사진을 프로필 사진으로 설정
//    var pictureUri: Uri? = null
//    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
//        if (it) {
//            pictureUri.let {
//                binding.myprofileImagePhoto.setImageURI(pictureUri)
//                new_image_uri = pictureUri
//            }
//        }
//    }
//
//    // 카메라 실행
//    private fun camera() {
//        pictureUri = createImageFile()
//        getTakePicture.launch(pictureUri)
//    }
//
//    // 카메라로 찍은 사진을 갤러리에 저장하기 위한 메소드(파일명 설정 등등)
//    private fun createImageFile(): Uri? {
//        val now = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
//        val content = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
//        }
//        return requireActivity().contentResolver.insert(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            content
//        )
//    }
}