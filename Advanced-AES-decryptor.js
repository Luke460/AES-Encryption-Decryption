
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


function decrypt(b64encryptedPayload, password) {
  // get key and iv from password
  var password = parseHexString(password); 
  var key = password.slice(0, 32);
  var iv = password.slice(32,48);
  var aesCfb = new aesjs.ModeOfOperation.cfb(key, iv, 16);

  // get encripted payload byte array 
  var encryptedPayload = atob(b64encryptedPayload);
  var encryptedPayloadArray = stringToBytes(encryptedPayload);
  
  // decrypt message
  var decryptedBytes = aesCfb.decrypt(encryptedPayloadArray);

  // get payload length
  var payloadLengthInfo = parseInt((aesjs.utils.utf8.fromBytes(decryptedBytes.slice(16, 32))));
  
  // extract payload
  var output = aesjs.utils.utf8.fromBytes(decryptedBytes.slice(32,32+payloadLengthInfo));
  
  return output;
}
 
