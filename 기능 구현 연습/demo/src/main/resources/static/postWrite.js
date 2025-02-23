$(document).ready(function() {
    let selectedFiles = [];

    $("#post-file").on("change", function(event) {
        let newFiles = Array.from(event.target.files);
        selectedFiles = selectedFiles.concat(newFiles); // 기존 파일 유지하면서 새로운 파일 추가

        // 중복 제거 (파일명 기준)
        selectedFiles = selectedFiles.filter((file, index, self) =>
        index === self.findIndex(f => f.name === file.name)
        );

        // 파일 목록 UI 업데이트
        $("#file-list").empty();
        $.each(selectedFiles, function(index, file) {
            $("#file-list").append(`<div>${file.name} (${(file.size / 1024).toFixed(2)} KB)</div>`);
        });

        // input의 files을 업데이트
        let dataTransfer = new DataTransfer();
        $.each(selectedFiles, function(index, file) {
            dataTransfer.items.add(file);
        });
        $("#post-file")[0].files = dataTransfer.files;
    });
});