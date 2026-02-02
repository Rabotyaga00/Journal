document.addEventListener('DOMContentLoaded', function() {
    var checkboxes = document.querySelectorAll('input[type="checkbox"][id^="presentCheck_"]');
    checkboxes.forEach(function(checkbox) {
        var index = checkbox.id.replace('presentCheck_', '');
        var hiddenInput = document.getElementById('presentHidden_' + index);

        if (hiddenInput) {
            hiddenInput.value = checkbox.checked ? 'true' : 'false';

            checkbox.addEventListener('change', function() {
                hiddenInput.value = this.checked ? 'true' : 'false';
            });
        }
    });
});
