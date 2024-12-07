const apiUrl = "http://localhost:8080/api/cities";

// Получение списка городов
function fetchCities() {
  fetch(apiUrl)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Ошибка получения данных");
      }
      return response.text();
    })
    .then((xml) => {
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(xml, "application/xml");
      const cities = Array.from(xmlDoc.getElementsByTagName("City"));

      const tableBody = document.getElementById("cities-table-body");
      tableBody.innerHTML = "";

      cities.forEach((city) => {
        const id = city.getElementsByTagName("id")[0].textContent;
        const name = city.getElementsByTagName("name")[0].textContent;
        const population = city.getElementsByTagName("population")[0].textContent;

        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${id}</td>
          <td>${name}</td>
          <td>${population}</td>
          <td>
            <button onclick="editCity(${id}, '${name}', ${population})">Редактировать</button>
            <button onclick="deleteCity(${id})">Удалить</button>
          </td>
        `;
        tableBody.appendChild(row);
      });
    })
    .catch((error) => console.error(error));
}

// Добавление или обновление города
function submitCity(event) {
  event.preventDefault();

  const id = document.getElementById("city-id").value;
  const name = document.getElementById("city-name").value;
  const population = document.getElementById("city-population").value;

  const cityData = `
    <City>
      ${id ? `<id>${id}</id>` : ""}
      <name>${name}</name>
      <population>${population}</population>
    </City>
  `;

  const method = id ? "PUT" : "POST";
  const url = id ? `${apiUrl}/${id}` : apiUrl;

  fetch(url, {
    method: method,
    headers: {
      "Content-Type": "application/xml",
    },
    body: cityData,
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Ошибка сохранения данных");
      }
      fetchCities();
      document.getElementById("city-form").reset();
    })
    .catch((error) => console.error(error));
}

// Удаление города
function deleteCity(id) {
  fetch(`${apiUrl}/${id}`, { method: "DELETE" })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Ошибка удаления города");
      }
      fetchCities();
    })
    .catch((error) => console.error(error));
}

// Редактирование города
function editCity(id, name, population) {
  document.getElementById("city-id").value = id;
  document.getElementById("city-name").value = name;
  document.getElementById("city-population").value = population;
}
