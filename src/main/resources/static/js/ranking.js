document.addEventListener('DOMContentLoaded', () => {
    /*
    TODO: [3단계] 인기 테마 - 인기 테마 목록 조회 API 호출
    */
    requestRead('/themes/rankings') // 인기 테마 목록 조회 API endpoint
        .then(render)
        .catch(error => console.error('Error fetching times:', error));
});

function render(data) {
    const container = document.getElementById('theme-ranking');

    /*
    TODO: [3단계] 인기 테마 - 인기 테마 목록 조회 API 호출 후 렌더링
          response 명세에 맞춰 name, thumbnail, description 값 설정
    */
    data.forEach(theme => {

        const htmlContent = `
            <img class="mr-3 img-thumbnail" src="${theme.thumbnail}" alt="${theme.name}" width="300">
            <div class="media-body">
                <h5 class="mt-0 mb-1">${theme.name}</h5>
                ${theme.description}
            </div>
        `;

        const div = document.createElement('li');
        div.className = 'media my-4';
        div.innerHTML = htmlContent;

        container.appendChild(div);
    })
}

function requestRead(endpoint) {
    return fetch(endpoint)
        .then(response => {
            if (response.status === 200) return response.json();
            throw new Error('Read failed');
        });
}
