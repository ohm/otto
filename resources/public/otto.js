$(function () {
  /**
   * Fork/Private/Public filters
   */
  $("form#filters").change(function () {
    var _fork = false,
        _priv = false,
        _pub  = false;

    $("form#filters input:checked").each(function () {
      switch ($(this).val()) {
        case "fork"    : _fork = true; break;
        case "private" : _priv = true; break;
        case "public"  : _pub  = true; break;
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

      if (!_pub && !elem.data("private")) {
        show = false;
      }

      return show ? elem.show() : elem.hide();
    });
  });

  /**
   * Simple search / string matching
   */
  var _searchTimeout = null;

  $("input#search").keyup(function () {
    if (_searchTimeout) {
      clearTimeout(_searchTimeout);
    };

    var searchTerm = $(this).val();
    setTimeout(function () {
      $("table tbody tr").each(function () {
        var elem = $(this),
            show = true;

        if (!RegExp(searchTerm, "i").test(elem.data("search"))) {
          show = false;
        }

        return show ? elem.show() : elem.hide();
      });
    }, 100);
  });

});
