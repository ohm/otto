$(function () {
  var update = function () {
    var fork = false,
        priv = false,
        pub  = false,
        term = $("input#search").val();

    $("form#filters input:checked").each(function () {
      switch ($(this).val()) {
        case "fork"    : fork = true; break;
        case "private" : priv = true; break;
        case "public"  : pub  = true; break;
      }
    });

    return $("table tbody tr").each(function () {
      var elem = $(this),
          show = true;

      if (!fork && elem.data("fork")) {
        show = false;
      }

      if (show && !priv && elem.data("private")) {
        show = false;
      }

      if (show && !pub && !elem.data("private")) {
        show = false;
      }

      if (show && (term.length > 0)) {
        if (!RegExp(term, "i").test(elem.data("search"))) {
          show = false;
        }
      }

      return show ? elem.show() : elem.hide();
    });
  };

  /**
   * Fork/Private/Public filters
   */
  $("form#filters").change(update);

  /**
   * Simple search / string matching
   */
  var searchTimeout = null;

  $("input#search").keyup(function () {
    if (searchTimeout !== null) {
      clearTimeout(searchTimeout);
    }

    searchTimeout = setTimeout(update, 100);
    return;
  });
});
