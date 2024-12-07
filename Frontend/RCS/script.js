const apiUrl = "http://localhost:8080/route";

// Рассчитать длину маршрута до самого старого города
function calculateToOldest() {
  fetch(`${apiUrl}/calculate/to-oldest`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Ошибка расчета маршрута до самого старого города");
      }
      return response.text();
    })
    .then((xml) => {
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(xml, "application/xml");
      const length = xmlDoc.getElementsByTagName("length")[0].textContent;

      document.getElementById("result-to-oldest").textContent = `Длина маршрута: ${length}`;
    })
    .catch((error) => {
      console.error(error);
      document.getElementById("result-to-oldest").textContent = "Ошибка при расчете маршрута.";
    });
}

// Рассчитать длину маршрута между старейшим и самым новым городом
function calculateBetweenOldestAndNewest() {
  fetch(`${apiUrl}/calculate/between-oldest-and-newest`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Ошибка расчета маршрута между старейшим и самым новым городом");
      }
      return response.text();
    })
    .then((xml) => {
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(xml, "application/xml");
      const length = xmlDoc.getElementsByTagName("length")[0].textContent;

      document.getElementById("result-between-oldest-newest").textContent = `Длина маршрута: ${length}`;
    })
    .catch((error) => {
      console.error(error);
      document.getElementById("result-between-oldest-newest").textContent = "Ошибка при расчете маршрута.";
    });
}
