$(function () {
  $("form#filters").change(function () {
    var _fork = false,
        _priv = false;

    $("form#filters input:checked").each(function () {
      switch ($(this).val()) {
        case "fork"    : _fork = true; break;
        case "private" : _priv = true; break;
      };
    });

    $("table tbody tr").each(function () {
      var elem = $(this),
          show = true;

      if (!_fork && elem.data("fork")) {
        show = false;
      };

      if (!_priv && elem.data("private")) {
        show = false;
      };

      return show ? elem.show() : elem.hide();
    });
  });
});
