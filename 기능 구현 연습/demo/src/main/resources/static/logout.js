$(document).ready(function () {
    let accessToken = localStorage.getItem("accessToken");

    // 액세스 토큰이 없으면 로그인 페이지로 이동
    if (!accessToken) {
        alert("로그인이 필요합니다.");
        window.location.href = "/signIn";  // 로그인 페이지로 이동
        return;
    }

    // 토큰 유효성 검증 API 요청
    $.ajax({
        url: "/validate-token",  // 서버에 토큰 검증을 요청할 엔드포인트
        type: "POST",
        contentType: "application/json",
        headers: {
            Authorization: "Bearer " + accessToken,
        },
        success: function (response) {
            console.log("토큰 유효함:", response);
            $("#logout").html(`
                <h3>로그아웃 페이지</h3>
                <button id="logout-btn" class="btn btn-danger">로그아웃</button>
            `);

            // 로그아웃 버튼 클릭 시
            $("#logout-btn").click(function () {
                localStorage.removeItem("accessToken");
                localStorage.removeItem("refreshToken");
                alert("로그아웃 되었습니다.");
                window.location.href = "/signIn";
            });
        },
        error: function (xhr) {
            console.log("토큰이 유효하지 않음:", xhr.responseText);
            alert("로그인이 만료되었습니다. 다시 로그인하세요.");
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            window.location.href = "/signIn";
        },
    });
});