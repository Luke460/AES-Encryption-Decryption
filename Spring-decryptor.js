// use this method if you want to retreive the SHA-384 from the Token.
function digestMessage(message) {
  const encoder = new TextEncoder();
  const data = encoder.encode(message);
  return window.crypto.subtle.digest('SHA-384', data);
}

function parseHexString(str) { 
    var result = [];
    while (str.length >= 2) { 
        var intValue = parseInt(str.substring(0, 2), 16);
        result.push(intValue);
        str = str.substring(2, str.length);
    }

    return result;
}

function stringToBytes(str) {
  var ch, st, re = [];
  for (var i = 0; i < str.length; i++ ) {
	ch = str.charCodeAt(i);  // get char 
	st = [];                 // set up "stack"
	do {
	  st.push( ch & 0xFF );  // push byte to stack
	  ch = ch >> 8;          // shift value down by 1 byte
	}  
	while ( ch );
	// add stack contents to result
	// done because chars have "wrong" endianness
	re = re.concat( st.reverse() );
  }
  // return an array of bytes
  return re;
}

function decrypt() {
  var b64encryptedPayload = $('#textInput').val();
  var hexPassword = $('#passwordInput').val();

  // get key and iv from password
  var password = parseHexString(hexPassword); 
  var key = password.slice(0, 32);
  var iv = password.slice(32,48);
  console.log('Key: '+ key.toString());
  console.log('IV:  '+ iv.toString());
  var aesCfb = new aesjs.ModeOfOperation.cfb(key, iv, 16);

  // get encripted payload from base64 encrypted payload 
  console.log('b64encryptedPayload: ' + b64encryptedPayload);
  var encryptedPayload = atob(b64encryptedPayload);
  console.log('encryptedPayload: ' + encryptedPayload.toString());
  
  // get encripted payload in byte array
  var encryptedPayloadArray = stringToBytes(encryptedPayload);
  console.log('encryptedPayloadArray: ' + encryptedPayloadArray.toString());
  
  // decrypt payload
  var decryptedBytes = aesCfb.decrypt(encryptedPayloadArray);
  console.log('decrypted payload in bytes: ' + decryptedBytes);

  // get decrypted text with paddings
  var decryptedText = aesjs.utils.utf8.fromBytes(decryptedBytes);
  console.log('full decrypted payload: ' + decryptedText.toString());

  // remove paddings
  var payloadLength = parseInt(decryptedText.substring(16, 32));
  var payload = decryptedText.substring(32,payloadLength+32)
  console.log('decrypted payload: ' + payload);

  return payload;

}
