// 로그인
$("#login").click(function () {
    email = $("#exampleInputEmail1").val();
    password = $("exampleInputPassword").val();
    if (!email) {
        alert("이메일을 입력하세요.");
        return;
    }

    if (!password) {
        alert("비밀번호를 입력하세요.");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/login",
        data: { email: email, password: password },
        success: function (response) {
            alert("로그인되었습니다.");
            window.location.href = "/logout";
        },
        error: function () {
            alert("로그인 실패");
        }
    });
});