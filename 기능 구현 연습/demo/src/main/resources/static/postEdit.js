$(document).ready(function () {
    let postId = window.location.pathname.split("/").pop();  // URL에서 ID 추출

    let selectedFiles = [];  // 새 파일 목록
    let existingFiles = [];  // 기존 파일 목록
    let deleteFiles = [];  // 삭제할 기존 파일 목록

    $("#cancel").attr("href", "/postView/" + postId); // 취소 버튼에 삽입

    if (postId) {
        // 기존 게시글 데이터 가져오기
        $.ajax({
            type: "GET",
            url: "/api/postView/" + postId,
            success: function (response) {
                $("#postId").val(response.id);
                $("#title").val(response.title);
                $("#content").val(response.content);

                existingFiles = response.images || []; // response.images가 null 또는 undefined이면 빈 배열([])을 기본값으로 할당
                updateFileList();
            },
            error: function () {
                alert("게시글 정보를 불러오는 데 실패했습니다.");
            }
        });
    }

    // 파일 선택 이벤트
    $("#post-file").on("change", function (event) {
        let newFiles = Array.from(event.target.files);
        selectedFiles = selectedFiles.concat(newFiles); // 새로 선택한 파일(newFiles)을 파일 목록(selectedFiles)에 추가

        // 중복 제거 (파일명 기준)
        selectedFiles = selectedFiles.filter((file, index, self) =>
        index === self.findIndex(f => f.name === file.name)
        );

        updateFileList();
    });

    // 파일 목록 UI 업데이트
    function updateFileList() {
        $("#file-list").empty();

        // 기존 이미지 목록 유지 및 삭제 버튼 추가
        existingFiles.forEach((fileUrl, index) => {
            $("#file-list").append(`
                <div class="file-item">
                    <img src="${fileUrl}" style="width:100px; height:auto;">
                    <button class="remove-file btn btn-sm btn-danger" data-type="existing" data-index="${index}">삭제</button>
                </div>
            `);
        });

        // 새로 추가된 파일 목록 표시
        selectedFiles.forEach((file, index) => {
            $("#file-list").append(`
                <div class="file-item">
                    ${file.name} (${(file.size / 1024).toFixed(2)} KB)
                    <button class="remove-file btn btn-sm btn-danger" data-type="new" data-index="${index}">삭제</button>
                </div>
            `);
        });
    }

    // 파일 삭제 버튼 클릭 이벤트
    $(document).on("click", ".remove-file", function () {
        let index = $(this).data("index");
        let type = $(this).data("type");

        if (type === "existing") {
            deleteFiles.push(existingFiles[index]); // 삭제할 파일 목록에 추가
            existingFiles.splice(index, 1); // existingFiles 배열에서 특정 인덱스(index)에 위치한 파일 1개를 삭제
        } else {
            selectedFiles.splice(index, 1);
        }

        updateFileList();
    });

    // 게시글 수정 요청
    $("#updatePost").click(function () {
        if (!confirm("수정하시겠습니까?")) return;

        let formData = new FormData();
        formData.append("postId", $("#postId").val());
        formData.append("title", $("#title").val());
        formData.append("content", $("#content").val());
        // 파일 목록(existingFiles)을 JSON 문자열로 변환 후 FormData에 추가
        // 배열(List<String>)이나 객체(Map<String, Object>) 같은 복잡한 데이터는 JSON 아니면 FormData에 바로 추가할 수 없다
        formData.append("existingImages", JSON.stringify(existingFiles)); // 유지할 파일
        formData.append("deleteFiles", JSON.stringify(deleteFiles)); // 삭제할 파일

        selectedFiles.forEach(file => formData.append("files", file));

        $.ajax({
            type: "POST",
            url: "/api/postViewUpdate/" + postId,
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                alert(response);
                window.location.href = "/postView/" + postId;
            },
            error: function () {
                alert("오류가 발생했습니다.");
            }
        });
    });
});
