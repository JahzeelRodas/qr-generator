function generateQR() {
    const contentInput = document.getElementById('content');
    const sizeInput = document.getElementById('size');
    const outputDiv = document.getElementById('qr-output');
    const messageDiv = document.getElementById('message');

    messageDiv.textContent = '';
    outputDiv.innerHTML = '<h2 class="text-xl font-semibold text-gray-800 mb-4">Código QR Generado:</h2>'; 

    const content = contentInput.value.trim();
    const size = parseInt(sizeInput.value);

    console.info(`[Frontend] Intentando generar QR. Contenido: "${content}", Tamaño: ${size}`);

    if (!content) {
        messageDiv.textContent = 'El contenido a codificar no puede estar vacío.';
        console.warn('[Frontend] Bloqueado: Contenido vacío.');
        return;
    }

    const apiUrl = `/generate?content=${encodeURIComponent(content)}&width=${size}&height=${size}`;

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                console.error(`[Frontend] Fallo en la llamada a la API. Código: ${response.status}`);
                
                return response.text().then(text => {
                    throw new Error(`Error ${response.status}: ${text || 'Detalle no proporcionado por el servidor.'}`);
                });
            }

            console.info('[Frontend] Respuesta de la API recibida exitosamente (Status 200).');
            return response.blob();
        })
        .then(imageBlob => {
            const imageObjectURL = URL.createObjectURL(imageBlob);
            
            const imgElement = document.createElement('img');
            imgElement.src = imageObjectURL;
            imgElement.alt = 'Código QR generado';
            imgElement.className = 'mt-4 mx-auto block border border-gray-300';
            
            outputDiv.appendChild(imgElement);
            console.info('[Frontend] Imagen QR insertada en el DOM.');

        })
        .catch(error => {
            console.error('[Frontend] Error crítico en la generación o comunicación:', error);
            messageDiv.textContent = `Fallo al generar el QR. ${error.message}`;
        });
}