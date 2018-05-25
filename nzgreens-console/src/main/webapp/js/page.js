function pageFunInit(searchFun) {
    $("div").delegate(".pageButton", "click", function (e) {
        e.stopPropagation();
        if ($(this).hasClass("curr-disable")) {
            return;
        }
        $("#pageNo").val($(this).attr("page"));
        $("#pageSize").val($("#inputPageSize").val());
        searchFun();
    });

    $("div").delegate("#inputPageSize", "keydown", function (e) {
        e.stopPropagation();
        if (e.keyCode == 13) {
            var oldPageSize = $(this).attr("oldValue");
            var pageSize = $(this).val();
            if (oldPageSize == pageSize) {
                return;
            }
            var time = new Date();
            var maxPageSize = 100;
            if (time.getHours() > 8 && time.getHours() < 12) {
                maxPageSize = 1000;
            }
            if (pageSize * 1 > maxPageSize) {
                pageSize = maxPageSize;
            }
            if (pageSize * 1 < 1) {
                pageSize = 1;
            }
            $("#pageNo").val("1");
            $("#pageSize").val(pageSize);
            searchFun();
        }
    });

    $("div").delegate("#inputPageNo", "keydown", function (e) {
        e.stopPropagation();
        if (e.keyCode == 13) {
            var oldPageNo = $(this).attr("oldValue");
            var totalPage = $("#totalPage").val();
            var pageNo = $(this).val();
            if (oldPageNo == pageNo) {
                return;
            }

            if (pageNo * 1 > totalPage * 1) {
                pageNo = totalPage;
            }
            if (pageNo < 1) {
                pageNo = 1;
            }
            $("#pageNo").val(pageNo);
            $("#pageSize").val($("#inputPageSize").val());
            searchFun();
        }
    });
}
