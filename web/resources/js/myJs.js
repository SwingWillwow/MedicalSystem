/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function myfun() {
    var target = event.target || event.srcElement;
    var parent = target.parentNode;
    layer.prompt({
        title: '请输入数量'
    }, function (value, index, elem) {
        if (value < 0 || value > 15) {
            layer.alert('输入数量要在0到15之间');
            layer.close(index);
        } else {
            var child = parent.childNodes;
            var number;
            for (i = 0; i < child.length; i++) {

                var str = child[i].id;
                if (str === undefined || str === "") {
                    continue;
                } else {
                    if (str.indexOf("hiddenForm") !== 0) {
                        number = str.substring(str.indexOf(":") + 1, str.lastIndexOf(":"));
                    }
                }
            }
            var hidden = document.getElementById("medicineTable:" + number + ":hiddenForm:hiddenNumber");
            hidden.value = value;
            var hiddenForm = document.getElementById("medicineTable:" + number + ":hiddenForm");
            alert('添加成功');
            layer.close(index);
            hiddenForm.submit();
        }

    });
}

