// --- CONFIGURACIÓN CRÍTICA ---
// Usamos la ruta raíz del propio servidor Spring Boot.
const API_BASE_URL = '';
// Se asume que Spring Boot inyecta la API Key.

// --- LÓGICA PRINCIPAL ENVUELTA EN DOMContentLoaded ---
document.addEventListener('DOMContentLoaded', () => {

    // --- VARIABLES GLOBALES ---
    let sessionId = null;
    const $ = (id) => document.getElementById(id);

    // --- REFERENCIAS DE ELEMENTOS DEL DOM ---
    const alertArea = $('alertArea');
    const step1 = $('step1');
    const step2 = $('step2');
    const loginForm = $('loginForm');
    const verifyForm = $('verifyForm');
    const finalResult = $('finalResult');
    const jsonResult = $('jsonResult');

    // --- UTILIDADES ---

    /**
     * Muestra alertas en la UI.
     */
    function showAlert(message, type = 'success') {
        const colors = {
            'success': {bg: 'bg-green-100', text: 'text-green-800', border: 'border-green-500'},
            'error': {bg: 'bg-red-100', text: 'text-red-800', border: 'border-red-500'},
            'info': {bg: 'bg-blue-100', text: 'text-blue-800', border: 'border-blue-500'}
        };
        const c = colors[type];

        const alertHtml = `
            <div class="${c.bg} ${c.text} p-3 rounded-lg border-l-4 ${c.border} shadow-sm fade-in" role="alert">
                <p class="font-medium">${type === 'success' ? 'Éxito' : type === 'error' ? 'Error' : 'Información'}:</p>
                <p class="text-sm">${message}</p>
            </div>
        `;
        alertArea.innerHTML = alertHtml;
    }

    /**
     * Activa o desactiva el estado de carga de un botón.
     * CORRECCIÓN: Se actualizó la convención de IDs para coincidir con el HTML.
     */
    function toggleLoading(buttonId, isLoading) {
        const button = $(buttonId);
        const textSpan = $(buttonId + 'Text'); // Ahora busca loginButtonText o verifyButtonText
        const spinner = $(buttonId + 'Spinner');

        if (!button || !textSpan || !spinner) {
             console.error(`Error: Elementos del botón ${buttonId} no encontrados.`);
             return;
        }

        button.disabled = isLoading;
        if (isLoading) {
            textSpan.classList.add('hidden');
            spinner.classList.remove('hidden');
        } else {
            textSpan.classList.remove('hidden');
            spinner.classList.add('hidden');
        }
    }

    /**
     * Realiza la transición visual entre los dos pasos del formulario.
     */
    function switchStep(fromStep, toStep) {
        fromStep.classList.replace('fade-in', 'fade-out');
        setTimeout(() => {
            fromStep.classList.add('hidden');
            toStep.classList.remove('hidden');
            toStep.classList.add('fade-in');
        }, 300);
    }


    // --- LÓGICA DEL LOGIN (PASO 1: /login) ---
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        toggleLoading('loginButton', true);
        alertArea.innerHTML = '';

        const documentNumber = $('documentNumber').value;

        const loginPayload = {
            documentNumber: documentNumber,
        };

        try {
            const response = await fetch(`${API_BASE_URL}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginPayload)
            });

            const data = await response.json();

            if (response.ok) {
                sessionId = data.id;
                $('sessionIdDisplay').textContent = `✅ Sesión ID obtenida: ${sessionId}`;
                showAlert('Inicio de sesión exitoso. Procede con la carga de imágenes.', 'success');

                switchStep(step1, step2);
            } else {
                showAlert(`Error ${response.status}: ${data.description || 'Credenciales inválidas.'}`, 'error');
            }
        } catch (error) {
            showAlert('Fallo de conexión. Revise la consola y asegúrese que Spring Boot esté corriendo.', 'error');
            console.error('Error en /login:', error);
        } finally {
            toggleLoading('loginButton', false);
        }
    });


    // --- LÓGICA DE VERIFICACIÓN (PASO 2: /identity) ---
    verifyForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        toggleLoading('verifyButton', true);
        alertArea.innerHTML = '';
        finalResult.classList.add('hidden');

        if (!sessionId) {
            showAlert('Error: No hay una sesión activa. Por favor, inicie sesión primero.', 'error');
            toggleLoading('verifyButton', false);
            return;
        }

        const formData = new FormData();
        const frontalFile = $('frontalFile').files[0];
        const dorsalFile = $('dorsalFile').files[0];

        formData.append('frontalFile', frontalFile);
        formData.append('dorsalFile', dorsalFile);

        try {
            const response = await fetch(`${API_BASE_URL}/identity/async`, {
                method: 'POST',
                headers: {
                    'session-id': sessionId,
                },
                body: formData
            });

            jsonResult.textContent = 'No response body';
            finalResult.classList.remove('hidden');
            window.scrollTo({ top: finalResult.offsetTop, behavior: 'smooth' });

            if (response.status === 202) {
                showAlert('🎉 ¡Verificación de DNI exitosa! Documento validado por Gemini.', 'success');
            } else {
                showAlert(`Error ${response.status}: Error del servidor o sesión inválida.`, 'error');
            }

        } catch (error) {
            showAlert('Fallo de conexión. Revise la consola y el `API_BASE_URL`.', 'error');
            console.error('Error en /identity:', error);
        } finally {
            toggleLoading('verifyButton', false);
        }
    });

}); // Fin de DOMContentLoaded