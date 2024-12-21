const apiUrl = "http://localhost:8080/route";

// Общая функция для выполнения запроса и обработки XML
function fetchAndProcessXml(url, resultElementId, successMessage, errorMessage) {
  fetch(url)
    .then((response) => {
      if (!response.ok) {
        throw new Error(errorMessage);
      }
      return response.text();
    })
    .then((xml) => {
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(xml, "application/xml");
      const distanceElement = xmlDoc.getElementsByTagName("distance")[0];

      if (!distanceElement) {
        throw new Error("Неверный формат XML: отсутствует тег <distance>");
      }

      const distance = distanceElement.textContent;
      document.getElementById(resultElementId).textContent = `Длина маршрута: ${distance}`;
      alert(successMessage);
    })
    .catch((error) => {
      console.error(error);
      document.getElementById(resultElementId).textContent = "Ошибка при расчете маршрута.";
      alert(error.message);
    });
}

// Рассчитать длину маршрута до самого старого города
function calculateToOldest() {
  fetchAndProcessXml(
    `${apiUrl}/calculate/to-oldest`,
    "result-to-oldest",
    "Расчет маршрута до самого старого города выполнен успешно!",
    "Ошибка расчета маршрута до самого старого города"
  );
}

// Рассчитать длину маршрута между старейшим и самым новым городом
function calculateBetweenOldestAndNewest() {
  fetchAndProcessXml(
    `${apiUrl}/calculate/between-oldest-and-newest`,
    "result-between-oldest-newest",
    "Расчет маршрута между старейшим и самым новым городом выполнен успешно!",
    "Ошибка расчета маршрута между старейшим и самым новым городом"
  );
}
