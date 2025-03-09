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
    let commentListContainer = $("#comment-list");

    $.ajax({
        type: "GET",
        url: "/api/postView/" + postId,
        success: function (post) {
            $("#post-title").text(post.title);
            $("#post-date").text(formatDate(post.updatedAt));
            $("#post-content").text(post.content);
            $("#post-nickname").text(post.nickname);
            $("#updatePost").attr("href", "/postViewUpdate/" + post.id); // 수정 버튼에 ID 삽입
            $("#updatePost").attr("data-nickname", post.nickname);
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

    $.ajax({
        type: "GET",
        url: "/api/postViewComments/" + postId,
        success: function (response) {
            // 댓글을 하나씩 순차적으로 추가
            response.forEach(function(comment) {
                const commentHtml = `
                    <div class="list-group-item">
                        <div class="d-flex w-100 justify-content-between">
                            <p class="mb-1" id="comment-content">${comment.content}</p>
                            <div class="d-flex align-items-center gap-2">
                                <small id="comment-nickname" class="mr-2">${comment.nickname}</small>
                                <small id="comment-createdAt" class="mr-2">${formatDate(comment.createdAt)}</small>
                                <button class="btn btn-sm btn-danger" data-id="${comment.id}" onclick="deleteComment('${comment.id}')">삭제</button>
                            </div>
                        </div>
                    </div>
                `;
                commentListContainer.append(commentHtml);  // 댓글 목록에 추가
            });
        },
        error: function () {
        }
    });
});

// 날짜 포맷 함수
function formatDate(dateString) {
    const date = new Date(dateString);
    return `${date.getFullYear()}-${('0' + (date.getMonth() + 1)).slice(-2)}-${('0' + date.getDate()).slice(-2)} ${('0' + date.getHours()).slice(-2)}:${('0' + date.getMinutes()).slice(-2)}`;
}

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
        error: function (xhr) {
            if (xhr.status === 403) {
                alert(xhr.responseText);
            } else {
                alert("삭제 중 오류가 발생했습니다.");
            }
        }
    });
});

// 댓글 작성
$(document).on("click", "#uploadComment", function () {
    if (!confirm("댓글을 작성하시겠습니까?")) {
        return false;
    }

    let content = $("#exampleFormControlTextarea1").val();
    let postId = window.location.pathname.split("/").pop();  // URL에서 ID 추출

    $.ajax({
        type: "POST",
        url: "/api/UploadComment",
        data: { content: content, postId: postId },
        success: function (response) {
            alert(response);
            location.reload();
        },
        error: function () {
            alert("작성 중 오류가 발생했습니다.");
        }
    });
});

// 댓글 삭제
function deleteComment(id) {
    let commentId = id;

    if (!confirm("댓글을 삭제하시겠습니까?")) {
        return false;
    }

    $.ajax({
        type: "POST",
        url: "/api/DeleteComment",
        data: { commentId: commentId },
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
}
