Proyecto para la materia Taller de Lenguajes II - 2024 - Facultad de Informática - UNLP
<img width="613" height="362" alt="image" src="https://github.com/user-attachments/assets/b985353e-8985-44c0-9af2-07143abb05e9" />
1.Introducción
El presente documento tiene como objetivo detallar el análisis funcional para el desarrollo
de una billetera virtual de criptomonedas. La solución propuesta busca proporcionar una
plataforma segura y fácil de usar para la gestión de activos digitales, realización de
transacciones y acceso a diversas funcionalidades avanzadas. Esta billetera está diseñada
para satisfacer las necesidades tanto de usuarios novatos como experimentados en el
ámbito de las criptomonedas.
2.Descripción General del Sistema
La billetera virtual es una aplicación que permite a los usuarios almacenar, enviar, recibir,
comprar y vender diversas criptomonedas. El sistema garantiza la seguridad de los fondos
mediante encriptación avanzada, autenticación de dos factores (2FA - Two Factor
Authentication), y otras medidas de seguridad. Además, ofrece una interfaz intuitiva para
que los usuarios monitoreen sus activos y realicen transacciones de manera sencilla.
Características principales
● Creación y gestión de cuentas de usuario con verificación de identidad.
● Soporte para múltiples criptomonedas (Bitcoin, Ethereum, Litecoin, entre otras).
● Funcionalidad para la compra y venta de criptomonedas utilizando monedas fiduciarias
o de uso corriente (también conocido por su nombre en inglés “fiat”).
● Intercambio de criptomonedas dentro de la plataforma (swap).
● Envío y recepción de criptomonedas a través de direcciones públicas.
● Monitoreo de precios de criptomonedas en tiempo real.
● Generación de reportes financieros y fiscales.
● Historial completo de transacciones.
● Integración con métodos de pago tradicionales.
● Servicio de soporte y asistencia al usuario disponible 24/7.
3. Funcionalidades del Sistema
3.1 Gestión de Cuentas de Usuario
Registro y autenticación: El sistema permitirá a los usuarios registrarse con sus datos
básicos: nombres completos, apellidos, fecha de nacimiento, país donde desea operar, un
e-mail y una contraseña segura. La autenticación de dos factores (2FA) será un requisito
obligatorio, enviando un código al dispositivo móvil del usuario para acceder a la cuenta. En
principio el usuario podrá elegir como segundo factor de autenticación que se envíe un SMS
a su número de teléfono móvil registrado ó a su casilla de e-mail.
Un requisito del área de Legales es que se registre además la aceptación de los términos y
condiciones del uso de la billetera al momento de la registración.
Verificación de identidad: Para cumplir con las normativas de KYC (Know Your Customer),
los usuarios deberán subir documentos de identidad oficiales y una fotografía reciente al
momento de la registración. Solo los usuarios verificados podrán acceder a todas las
funcionalidades de la billetera. Los documentos requeridos serán:
- una imagen de la cara anterior del DNI
- una imagen de la cara posterior del DNI
- una imagen del usuario sosteniendo el DNI
El sistema le ofrecerá al usuario la posibilidad de realizar este proceso luego de ingresar
sus datos de registración básicos, ó de realizarlos posteriormente antes de poder realizar
cualquier transacción en el sistema.
Configuración de seguridad: El sistema brindará a los usuarios la posibilidad de
personalizar su experiencia de seguridad, incluyendo la opción de habilitar notificaciones de
inicio de sesión que normalmente llegan a su casilla de e-mail y establecer 1 o más
preguntas de seguridad adicionales.
Verificaciones adicionales: Periódicamente el sistema consulta a otro sistema externo
(OFAC, Office of Foreign Assets Control), que verifica que el usuario de la billetera no se
encuentre dentro de una lista de personas “inhabilitadas” por diversos motivos: ser PEP
(persona políticamente expuesta) ó esté asociada a algún hecho ilícito, etc.
3.2 Gestión de Criptomonedas
Soporte para múltiples criptomonedas: Los usuarios podrán almacenar y gestionar
diversas criptomonedas en sus cuentas. En todo momento el usuario podrá acceder al
saldo asociado a cada criptomoneda, y al monto equivalente en moneda fiduciaria.
Visualización del saldo total (balance): El sistema proporcionará una vista consolidada del
saldo de todas las criptomonedas en la cuenta del usuario, permitiendo visualizar el valor
total en una moneda fiduciaria seleccionada por el usuario (si el usuario no especifica una
moneda fiduciaria específica, se asume la moneda del país seleccionado al momento de la
registración).
3.3 Compra y Venta de Criptomonedas
Compra de criptomonedas: Los usuarios podrán comprar criptomonedas utilizando su
saldo en moneda fiduciaria. El sistema mostrará las tasas de cambio en tiempo real, las
comisiones aplicables, y el monto total antes de que el usuario confirme la compra.
Venta de criptomonedas: Esta funcionalidad permitirá a los usuarios vender sus
criptomonedas y recibir el valor equivalente en moneda fiduciaria, que luego podrá ser
transferido a una cuenta bancaria o retenido en la billetera para futuras transacciones.
3.4 Intercambio entre Criptomonedas
Exchange interno (swap): Los usuarios podrán intercambiar diferentes criptomonedas
dentro de la misma plataforma utilizando tasas de mercado actuales. A modo de ejemplo,
un usuario que posee Litecoin podría cambiar al equivalente pero en Dogecoin. El sistema
le mostrará el detalle de esta transacción, incluyendo las comisiones asociadas.
3.5 Envío y Recepción de Criptomonedas
  Envío de criptomonedas: Los usuarios podrán transferir criptomonedas a otras direcciones
de billeteras externas, simplemente necesita especificar la dirección del destinatario. Esta
transacción podría o no ejecutarse de manera inmediata, depende de la blockchain que se
use para registrar la transacción. Y la transacción generalmente tiene un costo de comisión
que también depende de la blockchain. El usuario de la billetera generalmente no necesita
saber toda esta información, simplemente cuando quiera enviar cripto se le ofrecerán las
opciones de demora en envío con su costo y podrá elegir entre esas opciones.
Nuestra billetera consultará servicios externos para saber la información de demora y
costos, pero deberá registrar la operación realizada con todo el detalle asociado.
Recepción de criptomonedas:
Por cada moneda cripto que el usuario posea, el sistema generará una dirección la cual
quedará asociada a esa moneda para ese usuario. Es importante que el usuario indique con
exactitud esta dirección cuando quiera recibir cripto. El sistema notificará al usuario cuando
se reciban fondos.
3.6 DeFi (Decentralized Finance)
En el mundo cripto también existe algo parecido a los plazos fijos del mundo bancario
tradicional, aunque con mayor volatilidad en cuanto a los intereses que pueden generarse, y
de disponibilidad inmediata. En el mundo cripto estas opciones de DeFi se conocen como
“protocolos”, entre los más conocidos se encuentran AAVE, Yearn, Compound, etc. Nuestra
billetera consultará periódicamente los valores de intereses de cada protocolo para
ofrecerlas a los usuarios. Una vez que un usuario coloca una cantidad determinada de sus
cripto en un protocolo, ya no dispone de esa cripto para realizar otras operaciones,
debiendo retirarla de DeFi para tenerla a disposición nuevamente.
Nuestra billetera virtual cobrará intereses por la colocación de nuestra cripto en DeFi así
como por el retiro de los mismos.
A modo de ejemplo, DeFi podría brindar las siguientes opciones ofrecidas a los usuarios:
Moneda Protocolo Interés (APY)
DAI AAVE 4.98%
DAI Yearn 6.589%
USDC AAVE 2.54 %
USDC Yearn 3.145%
USDC Compound 5.1233%
Luego, un usuario que posee 800 USDC podría optar por colocar 250 USDC en Yearn, 450
USDC en Compound, dejando sólo 100 USDC de libre disponibilidad.
3.7 Monitoreo de Precios en Tiempo Real
Integración con APIs de mercado: El sistema se integrará con APIs que proporcionan datos
de precios en tiempo real. Los usuarios podrán configurar alertas de precios para ser
notificados cuando una criptomoneda alcance un valor específico.
3.8 Generación de Reportes Financieros
Historial de transacciones: El sistema almacenará un historial detallado de todas las
transacciones realizadas por el usuario, incluyendo compra, venta, intercambio, envío y
recepción de criptomonedas.
Reporte de Saldo de Cuentas: Se refiere a información sobre los saldos de las cuentas de
los usuarios, en particular aquellos usuarios que tengan activos por encima de cierto
umbral.
Registro de Altas y Bajas de Usuarios: Las billeteras deben mantener un registro detallado
de las altas y bajas de usuarios, debido a que otros organismos de control oficiales también
realizan verificaciones externas para evitar lavado de activos.
3.9 Tarjetas1
Nuestra billetera virtual brinda la posibilidad de obtener una tarjeta de débito VISA, la cual
estará asociada a una de nuestras criptomonedas, de modo que las transacciones que se
realicen con la tarjeta en moneda fiduciaria, en realidad se realizarán en cripto y al monto
equivalente de conversión al momento de la transacción.
Cuando un usuario quiere que se le otorgue una tarjeta de débito, debe aceptar términos y
condiciones. Por cuestiones legales, esa aceptación debe quedar registrada.
Un usuario puede hacer uso de la tarjeta de débito siempre que tenga saldo en la tarjeta de
débito elegida.
Existen planes a futuro de brindar la opción de tarjetas de crédito. En este caso, cuando se
implemente, la billetera virtual será la que, en base a la calificación del usuario decida si
puede o no operar con tarjeta de crédito o incluso inhabilitar luego de haber otorgado la
tarjeta. También establecerá los límites máximos de crédito por usuario. Si bien esta opción
no se encontrará disponible en el lanzamiento de la billetera, el sistema debe ser escalable
para luego ser incorporado.
3.10 Soporte y Asistencia al Usuario
Centro de ayuda: El sistema incluirá un centro de ayuda con respuestas a preguntas
frecuentes, guías y tutoriales.
Asistencia 24/7: El soporte técnico estará disponible a través de chat en vivo, correo
electrónico o llamada telefónica para resolver cualquier problema.
