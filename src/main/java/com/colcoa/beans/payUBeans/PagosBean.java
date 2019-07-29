package com.colcoa.beans.payUBeans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import com.colcoa.beans.payUBeans.dto.PayUConsignacionDTO;
import com.colcoa.beans.payUBeans.dto.PayUCreditCardDTO;
import com.colcoa.beans.payUBeans.dto.PayUPSEDTO;
import com.colcoa.enums.EnumBanco;
import com.colcoa.enums.EnumMedioPago;
import com.colcoa.enums.EnumTipoPersona;
import com.colcoa.enums.EnumTipoTarjeta;
import com.payu.sdk.PayU;
import com.payu.sdk.PayUPayments;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.model.Bank;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.DocumentType;
import com.payu.sdk.model.Language;
import com.payu.sdk.model.PaymentCountry;
import com.payu.sdk.model.TransactionResponse;
import com.payu.sdk.model.TransactionState;
import com.payu.sdk.utils.LoggerUtil;


@ManagedBean(name = "pagosBean")
@SessionScoped
public class PagosBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	String reference = "payment_test_00000001";
	String value = "20000";

	Map<String, String> parameters = new HashMap<String, String>();
	
	private PayUCreditCardDTO payUDTO;
	
	private PayUPSEDTO payUPSEDTO;
	
	private PayUConsignacionDTO payUConsignacionDTO;

	@PostConstruct
	public void Init() {
		payUDTO = new PayUCreditCardDTO();
		payUPSEDTO = new PayUPSEDTO();
		payUConsignacionDTO = new PayUConsignacionDTO();
	}
	
	public void creditCard(ActionEvent actionEvent) {
		// Ingrese aqu� el identificador de la cuenta.
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, "512321");
		// Ingrese aqu� el c�digo de referencia.
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, "" + reference);
		// Ingrese aqu� la descripci�n.
		parameters.put(PayU.PARAMETERS.DESCRIPTION, "payment test");
		// Ingrese aqu� el idima de la orden.
		parameters.put(PayU.PARAMETERS.LANGUAGE, "Language.es");

		// -- Valores --
		// Ingrese aqu� el valor de la transacci�n.
		parameters.put(PayU.PARAMETERS.VALUE, "" + value);
		
		// Ingrese aqu� el valor del IVA (Impuesto al Valor Agregado solo valido para
		// Colombia) de la transacci�n,
		// si se env�a el IVA nulo el sistema aplicar� el 19% autom�ticamente. Puede
		// contener dos d�gitos decimales.
		// Ej: 19000.00. En caso de no tener IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_VALUE, "0");
		
		// Ingrese aqu� el valor base sobre el cual se calcula el IVA (solo valido para
		// Colombia).
		// En caso de que no tenga IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_RETURN_BASE, "0");
		
		// Ingrese aqu� la moneda.
		parameters.put(PayU.PARAMETERS.CURRENCY, "" + Currency.COP.name());

		// -- Comprador --
		// Ingrese aqu� el id del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_ID, "1");
		
		// Ingrese aqu� el nombre del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_NAME, "First name and second buyer  name");
		
		// Ingrese aqu� el email del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, "buyer_test@test.com");
		// Ingrese aqu� el tel�fono de contacto del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_CONTACT_PHONE, "7563126");
		
		// Ingrese aqu� el documento de contacto del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_DNI, "5415668464654");
		
		// Ingrese aqu� la direcci�n del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_STREET, "calle 100");
		parameters.put(PayU.PARAMETERS.BUYER_STREET_2, "5555487");
		parameters.put(PayU.PARAMETERS.BUYER_CITY, "Medellin");
		parameters.put(PayU.PARAMETERS.BUYER_STATE, "Antioquia");
		parameters.put(PayU.PARAMETERS.BUYER_COUNTRY, "CO");
		parameters.put(PayU.PARAMETERS.BUYER_POSTAL_CODE, "000000");
		parameters.put(PayU.PARAMETERS.BUYER_PHONE, "7563126");

		// -- Pagador --
		// Ingrese aqu� el id del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_ID, "1");
		// Ingrese aqu� el nombre del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_NAME, "First name and second payer name");
		// Ingrese aqu� el email del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_EMAIL, "payer_test@test.com");
		// Ingrese aqu� el tel�fono de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_CONTACT_PHONE, "7563126");
		// Ingrese aqu� el documento de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_DNI, "5415668464654");
		// Ingrese aqu� la direcci�n del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_STREET, "calle 93");
		parameters.put(PayU.PARAMETERS.PAYER_STREET_2, "125544");
		parameters.put(PayU.PARAMETERS.PAYER_CITY, "Bogota");
		parameters.put(PayU.PARAMETERS.PAYER_STATE, "Bogota");
		parameters.put(PayU.PARAMETERS.PAYER_COUNTRY, "CO");
		parameters.put(PayU.PARAMETERS.PAYER_POSTAL_CODE, "000000");
		parameters.put(PayU.PARAMETERS.PAYER_PHONE, "7563126");

		// -- Datos de la tarjeta de cr�dito --
		// Ingrese aqu� el n�mero de la tarjeta de cr�dito
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER, payUDTO.getNumeroTarjeta().replaceAll("-", ""));
		// Ingrese aqu� la fecha de vencimiento de la tarjeta de cr�dito
		LocalDateTime localTime = payUDTO.getVencimientoTarjeta().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		String expirateDate = localTime.getYear() + "/" + (localTime.getMonth().getValue() <= 9 ? "0"+ localTime.getMonth().getValue() : localTime.getMonth().getValue());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE, expirateDate);
		// Ingrese aqu� el c�digo de seguridad de la tarjeta de cr�dito
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE, payUDTO.getCvc().toString());
		// Ingrese aqu� el nombre de la tarjeta de cr�dito
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, payUDTO.getTipoTarjeta().toString());

		// Ingrese aqu� el n�mero de cuotas.
		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "1");
		// Ingrese aqu� el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		// Session id del device.
		parameters.put(PayU.PARAMETERS.DEVICE_SESSION_ID, "vghs6tvkcle931686k1900o6e1");
		// IP del pagadador
		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "127.0.0.1");
		// Cookie de la sesi�n actual.
		parameters.put(PayU.PARAMETERS.COOKIE, "pt1t38347bs6jc9ruv2ecpv7o2");
		// Cookie de la sesi�n actual.
		parameters.put(PayU.PARAMETERS.USER_AGENT, "Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0");
		
		// Solicitud de autorizaci�n y captura
		TransactionResponse response;
		try {
			instancePayU();
			response = PayUPayments.doAuthorizationAndCapture(parameters);
			// Respuesta
			if (response != null) {
				response.getOrderId();
				response.getTransactionId();
				response.getState();
				if (response.getState().toString().equalsIgnoreCase("PENDING")) {
					response.getPendingReason();
				}
				response.getPaymentNetworkResponseCode();
				response.getPaymentNetworkResponseErrorMessage();
				response.getTrazabilityCode();
				response.getResponseCode();
				response.getResponseMessage();
			}
		} catch (PayUException | InvalidParametersException | ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void psePayment(ActionEvent actionEvent) {
		//Ingrese aqu� el nombre del medio de pago
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "PSE");
	
		//Ingrese aqu� el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());
		
		//Ingrese aqu� el identificador de la cuenta.
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, "512321");
		//Ingrese aqu� el c�digo de referencia.
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, ""+reference);
		//Ingrese aqu� la descripci�n.
		parameters.put(PayU.PARAMETERS.DESCRIPTION, "payment test");
		//Ingrese aqu� el idima de la orden.
		parameters.put(PayU.PARAMETERS.LANGUAGE, "Language.es");

		// -- Valores --
		//Ingrese aqu� el valor de la transacci�n.
		parameters.put(PayU.PARAMETERS.VALUE, ""+value);
		//Ingrese aqu� el valor del IVA (Impuesto al Valor Agregado solo valido para Colombia) de la transacci�n,
		//si se env�a el IVA nulo el sistema aplicar� el 19% autom�ticamente. Puede contener dos d�gitos decimales.
		//Ej: 19000.00. En caso de no tener IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_VALUE, "3193");
		//Ingrese aqu� el valor base sobre el cual se calcula el IVA (solo valido para Colombia).
		//En caso de que no tenga IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_RETURN_BASE, "16806");
		//Ingrese aqu� la moneda.
		parameters.put(PayU.PARAMETERS.CURRENCY, ""+Currency.COP.name());

		//Ingrese aqu� el email del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, "buyer_test@test.com");

		// -- pagador --
		//Ingrese aqu� el nombre del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_NAME, payUPSEDTO.getNombreTitular());
		//Ingrese aqu� el email del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_EMAIL, payUPSEDTO.getEmail());
		//Ingrese aqu� el tel�fono de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_CONTACT_PHONE, payUPSEDTO.getTelefono().toString());

		// -- infarmaci�n obligatoria para PSE --
		//Ingrese aqu� el c�digo pse del banco.
		List banks = getPseBanks();
		Iterator banks_iterator=banks.iterator();
		String pseBankCode = "";
		while(banks_iterator.hasNext()){
			Bank bank = (Bank) banks_iterator.next();
			
			if(bank.getId().equals(payUPSEDTO.getBanco().getMessage())) {
				pseBankCode = bank.getPseCode();
			}
		}
		
		parameters.put(PayU.PARAMETERS.PSE_FINANCIAL_INSTITUTION_CODE, pseBankCode);
		//Ingrese aqu� el tipo de persona (natural o jur�dica)
		parameters.put(PayU.PARAMETERS.PAYER_PERSON_TYPE, payUPSEDTO.getTipocliente().getMessage());
		//o parameters.put(PayU.PARAMETERS.PAYER_PERSON_TYPE, PersonType.LEGAL.toString() );
		//Ingrese aqu� el documento de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_DNI, payUPSEDTO.getNumeroIdentificacion().toString());
		//Ingrese aqu� el tipo de documento del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_DOCUMENT_TYPE, DocumentType.CC.toString());
		//IP del pagadador
		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "127.0.0.1");

		//Ingrese aqu� el nombre del medio de pago
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "PSE");

		//Ingrese aqu� el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		//Cookie de la sesi�n actual.
		parameters.put(PayU.PARAMETERS.COOKIE, "pt1t38347bs6jc9ruv2ecpv7o2");
		//Cookie de la sesi�n actual.
		parameters.put(PayU.PARAMETERS.USER_AGENT, "Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0");

		//P�gina de respuesta a la cual ser� redirigido el pagador.
		parameters.put(PayU.PARAMETERS.RESPONSE_URL, "http://www.test.com/response");

		//Solicitud de autorizaci�n y captura
		TransactionResponse response;
		try {
			response = PayUPayments.doAuthorizationAndCapture(parameters);
			//Respuesta
			if(response != null){
				response.getOrderId();
				response.getTransactionId();
				response.getState();
				if(response.getState().equals(TransactionState.PENDING)){
					response.getPendingReason();
					Map extraParameters = response.getExtraParameters();

					//obtener la url del banco
					String url=(String)extraParameters.get("BANK_URL");

				}
				response.getPaymentNetworkResponseCode();
				response.getPaymentNetworkResponseErrorMessage();
				response.getTrazabilityCode();
				response.getResponseCode();
				response.getResponseMessage();
			}
		} catch (PayUException | InvalidParametersException | ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List getPseBanks() {
		//Ingrese aqu� el nombre del medio de pago
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "PSE");
	
		//Ingrese aqu� el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());
	
		//Obtener el listado de bancos disponibles
		List banks;
		try {
			banks = PayUPayments.getPSEBanks(parameters);
			return banks;
		} catch (PayUException | InvalidParametersException | ConnectionException e) {
			e.printStackTrace();
			return null;
		}		
	}

	private void instancePayU() {
		PayU.apiKey = "8KB94yZmS1xu19eF8aKGYG9Nd4"; //Ingresa aqu� tu apiKey.
		PayU.apiLogin = "bYC5fH2p5T270G6"; //Ingresa aqu� tu apiLogin.
		PayU.language = Language.es; //Ingresa aqu� el idioma que prefieras.
		PayU.isTest = true; //Dejarlo verdadero cuando sean pruebas.
		LoggerUtil.setLogLevel(Level.ALL); //Incluirlo �nicamente si desea ver toda la traza del log; si solo se desea ver la respuesta, se puede eliminar.
		PayU.paymentsUrl = "https://api.payulatam.com/payments-api/"; //Incluirlo �nicamente si desea probar en un servidor de pagos espec�fico, e indicar la ruta del mismo.
		PayU.reportsUrl = "https://api.payulatam.com/reports-api/"; //Incluirlo �nicamente si desea probar en un servidor de reportes espec�fico, e indicar la ruta del mismo
	}
	
	public void consignacion(ActionEvent actionEvent) {
		Map<String, String> parameters = new HashMap<String, String>();

		//Ingrese aqu� el identificador de la cuenta.
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, "512321");
		//Ingrese aqu� el c�digo de referencia.
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, ""+reference);
		//Ingrese aqu� la descripci�n.
		parameters.put(PayU.PARAMETERS.DESCRIPTION, "payment test");
		//Ingrese aqu� el idima de la orden.
		parameters.put(PayU.PARAMETERS.LANGUAGE, "Language.es");

		// -- Valores --
		//Ingrese aqu� el valor de la transacci�n.
		parameters.put(PayU.PARAMETERS.VALUE, ""+value);
		//Ingrese aqu� el valor del IVA (Impuesto al Valor Agregado solo valido para Colombia) de la transacci�n,
		//si se env�a el IVA nulo el sistema aplicar� el 19% autom�ticamente. Puede contener dos d�gitos decimales.
		//Ej: 19000.00. En caso de no tener IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_VALUE, "3193");
		//Ingrese aqu� el valor base sobre el cual se calcula el IVA (solo valido para Colombia).
		//En caso de que no tenga IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_RETURN_BASE, "16806");
		//Ingrese aqu� la moneda.
		parameters.put(PayU.PARAMETERS.CURRENCY, ""+Currency.COP.name());

		//Ingrese aqu� el email del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, "buyer_test@test.com");

		//Ingrese aqu� el nombre del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_NAME, "First name and second payer name");

		//Ingrese aqu� el nombre del medio de pago en efectivo
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, payUConsignacionDTO.getMedioPago().getMessage());

		//Ingrese aqu� el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		//Ingrese aqu� la fecha de expiraci�n.
		parameters.put(PayU.PARAMETERS.EXPIRATION_DATE,"2014-05-20T00:00:00");

		//IP del pagadador
		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "127.0.0.1");

		//Solicitud de autorizaci�n y captura
		TransactionResponse response;
		try {
			response = PayUPayments.doAuthorizationAndCapture(parameters);
			
			//Respuesta
			if(response != null){
				response.getOrderId();
				response.getTransactionId();
				response.getState();
				if(response.getState().equals(TransactionState.PENDING)){
					response.getPendingReason();
					Map extraParameters = response.getExtraParameters();

					//obtener la url del comprobante de pago
					String url=(String)extraParameters.get("URL_PAYMENT_RECEIPT_HTML");
				}
				response.getPaymentNetworkResponseCode();
				response.getPaymentNetworkResponseErrorMessage();
				response.getTrazabilityCode();
				response.getResponseCode();
				response.getResponseMessage();
			}
		} catch (PayUException | InvalidParametersException | ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public PayUPSEDTO getPayUPSEDTO() {
		return payUPSEDTO;
	}
	
	public void setPayUPSEDTO(PayUPSEDTO payUPSEDTO) {
		this.payUPSEDTO = payUPSEDTO;
	}
	
	public PayUCreditCardDTO getPayUDTO() {
		return payUDTO;
	}

	public void setPayUDTO(PayUCreditCardDTO payUDTO) {
		this.payUDTO = payUDTO;
	}
	
	public PayUConsignacionDTO getPayUConsignacionDTO() {
		return payUConsignacionDTO;
	}
	
	public void setPayUConsignacionDTO(PayUConsignacionDTO payUConsignacionDTO) {
		this.payUConsignacionDTO = payUConsignacionDTO;
	}

	public EnumTipoTarjeta[] getTiposTarjeta() {
		return EnumTipoTarjeta.values();
	}
	
	public EnumBanco[] getBancos() {
		return EnumBanco.values();
	}
	
	public EnumTipoPersona[] getTiposPersona() {
		return EnumTipoPersona.values();
	}
	
	public EnumMedioPago[] getMediosPago() {
		return EnumMedioPago.values();
	}
}
