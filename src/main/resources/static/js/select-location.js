$(document).ready(function() {
    $('#region').change(function() {
        const region = $(this).val();
        if (region) {
            $.get('/location/districts', { region: region })
                .done(function(districts) {
                    $('#district').prop('disabled', false);
                    $('#district').empty()
                        .append('<option value="">선택하세요</option>');
                    districts.forEach(function(district) {
                        $('#district').append(
                            $('<option></option>').val(district).text(district)
                        );
                    });
                    $('#town').prop('disabled', true)
                        .empty()
                        .append('<option value="">선택하세요</option>');
                    $('#saveButton').prop('disabled', true);
                });
        } else {
            $('#district').prop('disabled', true);
            $('#town').prop('disabled', true);
            $('#saveButton').prop('disabled', true);
        }
    });

    $('#district').change(function() {
        const region = $('#region').val();
        const district = $(this).val();
        if (district) {
            $.get('/location/towns', {
                region: region,
                district: district
            })
            .done(function(towns) {
                $('#town').prop('disabled', false);
                $('#town').empty()
                    .append('<option value="">선택하세요</option>');
                towns.forEach(function(town) {
                    $('#town').append(
                        $('<option></option>').val(town).text(town)
                    );
                });
                $('#saveButton').prop('disabled', true);
            });
        } else {
            $('#town').prop('disabled', true);
            $('#saveButton').prop('disabled', true);
        }
    });

    $('#town').change(function() {
        $('#saveButton').prop('disabled', !$(this).val());
    });

    $('#saveButton').click(function() {
        const region = $('#region').val();
        const district = $('#district').val();
        const town = $('#town').val();

        $.post('/location/save', {
            region: region,
            district: district,
            town: town
        })
        .done(function(response) {
            alert('지역 설정이 완료되었습니다.');
            window.location.href = '/'; // 메인 페이지로 이동
        })
        .fail(function(xhr) {
            alert('지역 설정 중 오류가 발생했습니다.');
        });
    });
});
