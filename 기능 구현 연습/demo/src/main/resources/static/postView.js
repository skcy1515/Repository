// 날짜 변환 함수 추가 (script 파일 내부에서 정의)
function formatDate(dateString) {
    let date = new Date(dateString);
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, '0');
    let day = String(date.getDate()).padStart(2, '0');
    let hours = String(date.getHours()).padStart(2, '0');
    let minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

$(document).ready(function () {
    let postId = window.location.pathname.split("/").pop();  // URL에서 ID 추출

    $.ajax({
        type: "GET",
        url: "/api/postView/" + postId,
        success: function (post) {
            $("#post-title").text(post.title);
            $("#post-date").text(formatDate(post.updatedAt));
            $("#post-content").text(post.content);
            $("#updatePost").attr("href", "/postViewUpdate/" + post.id); // 수정 버튼에 ID 삽입
            $("#deletePost").attr("data-id", post.id); // 삭제 버튼에 ID 삽입

            // 이미지가 있는 경우 표시
            if (post.images && post.images.length > 0) {
                let imgHtml = "";
                post.images.forEach(function (img) {
                    imgHtml += `<img src="${img}"><p>`;
                });
                $("#post-images").html(imgHtml);
            }
        },
        error: function () {
            alert("게시글을 불러오는데 실패했습니다.");
        }
    });
});

// 포스트 삭제
$(document).on("click", "#deletePost", function () {
    if (!confirm("삭제하시겠습니까?")) {
        return false;
    }

    let postId = window.location.pathname.split("/").pop();  // URL에서 ID 추출

    $.ajax({
        type: "POST",
        url: "/api/postViewDelete/" + postId,
        success: function (response) {
            alert(response);
            window.location.href = "/getAllPosts";
        },
        error: function () {
            alert("오류가 발생했습니다.");
        }
    });
});