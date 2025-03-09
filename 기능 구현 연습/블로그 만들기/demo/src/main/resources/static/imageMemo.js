// 포스팅박스 열기 닫기
$(document).ready(function() {
    $("#btn-posting-box").on("click", function() {
        let $postBox = $("#post-box");
        let isVisible = $postBox.is(":visible");

        $postBox.toggle();
        $(this).text(isVisible ? "입력박스 열기" : "입력박스 닫기");
    });
});

// 포스트 삭제
$(document).on("click", ".btn-delete", function () {
    let imageId = $(this).attr("data-id");

    if (!confirm("정말 삭제하시겠습니까?")) {
        return false;
    }

    $.ajax({
        type: "POST",
        url: "/imageMemoDelete",
        data: { imageId: imageId },
        success: function (response) {
            alert(response);
            location.reload();
        },
        error: function (xhr) {
            if (xhr.status === 403) {
                alert(xhr.responseText);
            } else if (xhr.status === 404) {
                alert(xhr.responseText);
            } else {
                alert("삭제 중 오류가 발생했습니다.");
            }
        }
    });
});

// 포스트 전체 삭제
$(document).on("click", "#deleteAll", function () {

    if (!confirm("정말 전부 삭제하시겠습니까?")) {
        return false;
    }

    $.ajax({
        type: "POST",
        url: "/imageMemoDeletes",
        success: function (response) {
            alert(response);
            location.reload();
        },
        error: function (xhr) {
            if (xhr.status === 403) {
                alert(xhr.responseText);
            } else {
                alert("삭제 중 오류가 발생했습니다.");
            }
        }
    });
});