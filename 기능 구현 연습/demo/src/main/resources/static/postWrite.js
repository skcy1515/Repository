$(document).ready(function() {
    let selectedFiles = [];

    $("#post-file").on("change", function(event) {
        let newFiles = Array.from(event.target.files);
        selectedFiles = selectedFiles.concat(newFiles); // 기존 파일 유지하면서 새로운 파일 추가

        // 중복 제거 (파일명 기준)
        selectedFiles = selectedFiles.filter((file, index, self) =>
        index === self.findIndex(f => f.name === file.name)
        );

        updateFileList();
    });

    // 파일 목록 UI 업데이트 함수
    function updateFileList() {
        $("#file-list").empty();
        $.each(selectedFiles, function(index, file) {
            $("#file-list").append(`
                <div class="file-item">
                    ${file.name} (${(file.size / 1024).toFixed(2)} KB)
                    <button class="remove-file" data-index="${index}">삭제</button>
                </div>
            `);
        });

        // input의 files을 업데이트
        let dataTransfer = new DataTransfer();
        $.each(selectedFiles, function(index, file) {
            dataTransfer.items.add(file);
        });
        $("#post-file")[0].files = dataTransfer.files;
    }

    // 파일 삭제 버튼 클릭 이벤트
    $(document).on("click", ".remove-file", function() {
        let index = $(this).data("index");
        selectedFiles.splice(index, 1); // 배열에서 해당 파일 제거
        updateFileList(); // UI 및 input 갱신
    });
});

// 포스트 업로드
$(document).on("click", "#postUpload", function () {
    if (!confirm("작성하시겠습니까?")) {
        return false;
    }

    let formData = new FormData();  // FormData 객체 생성 (파일 포함 가능)

    // 제목과 내용 추가
    formData.append("title", $("#title").val());
    formData.append("content", $("#content").val());

    // 파일 추가 (다중 파일 업로드 가능)
    let files = $("#post-file")[0].files;
    for (let i = 0; i < files.length; i++) {
        formData.append("files", files[i]);
    }

    $.ajax({
        type: "POST",
        url: "/postUpload",
        data: formData,
        processData: false,  // 파일 업로드 시 필수 (데이터 변환 방지)
        contentType: false,  // 파일 업로드 시 필수 (기본 contentType 설정 안함)
        success: function (response) {
            alert(response);
            window.location.href = "/getAllPosts";
        },
        error: function () {
            alert("오류가 발생했습니다.");
        }
    });
});
