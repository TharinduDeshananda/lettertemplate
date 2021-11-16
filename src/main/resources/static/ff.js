console.log('hello world!!');
$(document).ready(function() {
    var $dragging = null;

    $(document.body).on("mousemove", function(e) {
        if ($dragging) {
            $dragging.offset({
                top: e.pageY,
                left: e.pageX
            });
        }
    });

    $(document.body).on("mousedown", ".innerDiv", function (e) {
        $dragging = $(e.target.parentElement);
        return true
    });

    $(document.body).on("mouseup", function (e) {
        $dragging = null;
        return true;
    });
});
