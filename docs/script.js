const API_URL = "http://localhost:8080"

// Get DOM elements
const keyInput = document.getElementById("keyInput")
const valueInput = document.getElementById("valueInput")
const putBtn = document.getElementById("putBtn")
const getBtn = document.getElementById("getBtn")
const displayBtn = document.getElementById("displayBtn")
const resultDiv = document.getElementById("result")
const cacheDisplay = document.getElementById("cacheDisplay")

// Event listeners
putBtn.addEventListener("click", handlePut)
getBtn.addEventListener("click", handleGet)
displayBtn.addEventListener("click", handleDisplay)

// Allow Enter key to trigger operations
keyInput.addEventListener("keypress", (e) => {
  if (e.key === "Enter") handleGet()
})

valueInput.addEventListener("keypress", (e) => {
  if (e.key === "Enter") handlePut()
})

/**
 * Handle PUT operation
 */
async function handlePut() {
  const key = keyInput.value.trim()
  const value = valueInput.value.trim()

  if (!key || !value) {
    showResult("Error: Please enter both key and value", "error")
    return
  }

  try {
    const response = await fetch(`${API_URL}/put?key=${key}&value=${value}`)
    const data = await response.json()
    showResult(`✓ ${data.message}`, "success")
    clearInputs()
    await handleDisplay()
  } catch (error) {
    showResult(`Error: ${error.message}`, "error")
  }
}

/**
 * Handle GET operation
 */
async function handleGet() {
  const key = keyInput.value.trim()

  if (!key) {
    showResult("Error: Please enter a key", "error")
    return
  }

  try {
    const response = await fetch(`${API_URL}/get?key=${key}`)
    const data = await response.json()

    if (data.value === -1) {
      showResult(`Key ${key} not found in cache`, "info")
    } else {
      showResult(`✓ Key ${key}: Value = ${data.value}`, "success")
    }
    await handleDisplay()
  } catch (error) {
    showResult(`Error: ${error.message}`, "error")
  }
}

/**
 * Handle DISPLAY operation
 */
async function handleDisplay() {
  try {
    const response = await fetch(`${API_URL}/display`)
    const data = await response.json()

    if (data.cache.length === 0) {
      cacheDisplay.innerHTML = '<p class="placeholder">Cache is empty</p>'
    } else {
      let html = `<strong>Cache Contents (${data.size}/${data.capacity}):</strong><br><br>`
      data.cache.forEach((item, index) => {
        html += `${index + 1}. Key: ${item.key}, Value: ${item.value}<br>`
      })
      cacheDisplay.innerHTML = html
    }
  } catch (error) {
    cacheDisplay.innerHTML = `<p class="placeholder">Error loading cache: ${error.message}</p>`
  }
}

/**
 * Display result message
 */
function showResult(message, type) {
  resultDiv.innerHTML = message
  resultDiv.style.color = type === "error" ? "#dc3545" : type === "success" ? "#28a745" : "#0c5460"
  resultDiv.style.backgroundColor = type === "error" ? "#f8d7da" : type === "success" ? "#d4edda" : "#d1ecf1"
}

/**
 * Clear input fields
 */
function clearInputs() {
  keyInput.value = ""
  valueInput.value = ""
  keyInput.focus()
}

// Load initial cache state
handleDisplay()
