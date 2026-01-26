// Инициализация значений при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    var checkboxes = document.querySelectorAll('input[type="checkbox"][id^="presentCheck_"]');
    checkboxes.forEach(function(checkbox) {
        var index = checkbox.id.replace('presentCheck_', '');
        var hiddenInput = document.getElementById('presentHidden_' + index);

        if (hiddenInput) {
            // Устанавливаем начальное значение
            hiddenInput.value = checkbox.checked ? 'true' : 'false';

            // Добавляем обработчик изменения
            checkbox.addEventListener('change', function() {
                hiddenInput.value = this.checked ? 'true' : 'false';
            });
        }
    });
});
