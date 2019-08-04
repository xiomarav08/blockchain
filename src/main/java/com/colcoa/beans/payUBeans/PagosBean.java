package com.colcoa.beans.payUBeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.colcoa.beans.TransactionBean;
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
import com.payu.sdk.utils.LoggerUtil;


@ManagedBean(name = "pagosBean")
@SessionScoped
public class PagosBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//TODO generar un numero de referencia de cada transaccion
	private String reference = "payment_test_00000001";
	
	private String value;
	
	private PayUCreditCardDTO payUDTO;
	
	private PayUPSEDTO payUPSEDTO;
	
	private PayUConsignacionDTO payUConsignacionDTO;
	
	private Boolean renderCreditCard;
	
	private Boolean renderPSE;
	
	private Boolean renderConsignacion;
	
	private String headerModal;
	
	private Properties properties;
	
	private Integer numeroArboles;
	
	private Integer numeroMonedas;
	
	@Inject
	private TransactionBean transactionBean;
	

	@PostConstruct
	public void Init() {
		payUDTO = new PayUCreditCardDTO();
		payUPSEDTO = new PayUPSEDTO();
		payUConsignacionDTO = new PayUConsignacionDTO();
		try {
			properties = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("payment.properties");
			properties.load(inputStream);
		}catch (IOException e) {
			// TODO: handle exception
		}
	}
	
	
	public void processPay(String optionValue) {
		if(optionValue.equals("creditCard")) {
			creditCard();
		}else if(optionValue.equals("pse")) {
			psePayment();
		}else if(optionValue.equals("consignacion")) {
			consignacion();
		}
	}
	
	private void creditCard() {
		Map<String, String> parameters = buyerInformation();
		
		// -- Valores --
		//Ingrese aquí el valor de la transacción.
		parameters.put(PayU.PARAMETERS.VALUE, ""+value);
		
		// -- Pagador --
		// Ingrese aquí el id del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_ID, "1");
		// Ingrese aquí el nombre del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_NAME, "First name and second payer name");
		// Ingrese aquí el email del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_EMAIL, "payer_test@test.com");
		// Ingrese aquí el teléfono de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_CONTACT_PHONE, "7563126");
		// Ingrese aquí el documento de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_DNI, "5415668464654");
		// Ingrese aquí la dirección del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_STREET, "calle 93");
		parameters.put(PayU.PARAMETERS.PAYER_STREET_2, "125544");
		parameters.put(PayU.PARAMETERS.PAYER_CITY, "Bogota");
		parameters.put(PayU.PARAMETERS.PAYER_STATE, "Bogota");
		parameters.put(PayU.PARAMETERS.PAYER_COUNTRY, "CO");
		parameters.put(PayU.PARAMETERS.PAYER_POSTAL_CODE, "000000");
		parameters.put(PayU.PARAMETERS.PAYER_PHONE, "7563126");

		// -- Datos de la tarjeta de crédito --
		// Ingrese aquí el número de la tarjeta de crédito
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER, payUDTO.getNumeroTarjeta().replaceAll("-", ""));
		// Ingrese aquí la fecha de vencimiento de la tarjeta de crédito
		LocalDateTime localTime = payUDTO.getVencimientoTarjeta().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		String expirateDate = localTime.getYear() + "/" + (localTime.getMonth().getValue() <= 9 ? "0"+ localTime.getMonth().getValue() : localTime.getMonth().getValue());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE, expirateDate);
		// Ingrese aquí el código de seguridad de la tarjeta de crédito
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE, payUDTO.getCvc().toString());
		// Ingrese aquí el nombre de la tarjeta de crédito
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, payUDTO.getTipoTarjeta().toString());

		// Ingrese aquí el número de cuotas.
		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "1");
		// Ingrese aquí el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		// Session id del device.
		parameters.put(PayU.PARAMETERS.DEVICE_SESSION_ID, "vghs6tvkcle931686k1900o6e1");
		// IP del pagadador
		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "127.0.0.1");
		// Cookie de la sesión actual.
		parameters.put(PayU.PARAMETERS.COOKIE, "pt1t38347bs6jc9ruv2ecpv7o2");
		// Cookie de la sesión actual.
		parameters.put(PayU.PARAMETERS.USER_AGENT, "Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0");
		transactionBean.cargarBalance(numeroArboles);
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "billetera.xhtml");
		
//		// Solicitud de autorización y captura
//		TransactionResponse response;
//		try {
//			instancePayU();
//			response = PayUPayments.doAuthorizationAndCapture(parameters);
//			// Respuesta
//			if (response != null) {
//				response.getOrderId();
//				response.getTransactionId();
//				response.getState();
//				if (response.getState().toString().equalsIgnoreCase("PENDING")) {
//					response.getPendingReason();
//				}
//				response.getPaymentNetworkResponseCode();
//				response.getPaymentNetworkResponseErrorMessage();
//				response.getTrazabilityCode();
//				response.getResponseCode();
//				response.getResponseMessage();
//				response.setResponseCode(TransactionResponseCode.APPROVED);
//				if(TransactionResponseCode.APPROVED.equals(response.getResponseCode())) {
//					
//				}
//			}
//		} catch (PayUException | InvalidParametersException | ConnectionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void psePayment() {
		
		Map<String, String> parameters = buyerInformation();
		
		//Ingrese aquí el nombre del medio de pago
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "PSE");
	
		//Ingrese aquí el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());
		
		
		// -- Valores --
		//Ingrese aquí el valor de la transacción.
		parameters.put(PayU.PARAMETERS.VALUE, ""+value);
		
		// -- pagador --
		//Ingrese aquí el nombre del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_NAME, payUPSEDTO.getNombreTitular());
		//Ingrese aquí el email del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_EMAIL, payUPSEDTO.getEmail());
		//Ingrese aquí el teléfono de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_CONTACT_PHONE, payUPSEDTO.getTelefono().toString());

		// -- infarmación obligatoria para PSE --
		//Ingrese aquí el código pse del banco.
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
		//Ingrese aquí el tipo de persona (natural o jurídica)
		parameters.put(PayU.PARAMETERS.PAYER_PERSON_TYPE, payUPSEDTO.getTipocliente().getMessage());
		//o parameters.put(PayU.PARAMETERS.PAYER_PERSON_TYPE, PersonType.LEGAL.toString() );
		//Ingrese aquí el documento de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_DNI, payUPSEDTO.getNumeroIdentificacion().toString());
		//Ingrese aquí el tipo de documento del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_DOCUMENT_TYPE, DocumentType.CC.toString());
		//IP del pagadador
		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "127.0.0.1");

		//Cookie de la sesión actual.
		parameters.put(PayU.PARAMETERS.COOKIE, "pt1t38347bs6jc9ruv2ecpv7o2");
		//Cookie de la sesión actual.
		parameters.put(PayU.PARAMETERS.USER_AGENT, "Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0");

		//Página de respuesta a la cual será redirigido el pagador.
		parameters.put(PayU.PARAMETERS.RESPONSE_URL, "http://www.test.com/response");
		
		transactionBean.cargarBalance(numeroArboles);
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "billetera.xhtml");

		//Solicitud de autorización y captura
//		TransactionResponse response;
//		try {
//			instancePayU();
//			response = PayUPayments.doAuthorizationAndCapture(parameters);
//			//Respuesta
//			if(response != null){
//				response.getOrderId();
//				response.getTransactionId();
//				response.getState();
//				if(response.getState().equals(TransactionState.PENDING)){
//					response.getPendingReason();
//					Map extraParameters = response.getExtraParameters();
//
//					//obtener la url del banco
//					String url=(String)extraParameters.get("BANK_URL");
//
//				}
//				response.getPaymentNetworkResponseCode();
//				response.getPaymentNetworkResponseErrorMessage();
//				response.getTrazabilityCode();
//				response.getResponseCode();
//				response.getResponseMessage();
//				response.setResponseCode(TransactionResponseCode.APPROVED);
//				if(TransactionResponseCode.APPROVED.equals(response.getResponseCode())) {
//					
//				}
//			}
//		} catch (PayUException | InvalidParametersException | ConnectionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private Map<String, String> buyerInformation() {			
		Map<String, String> parameters = new HashMap<>();
		
		// Ingrese aquí el identificador de la cuenta.
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, properties.getProperty("ACCOUNT_ID"));
		// Ingrese aquí el código de referencia.
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, properties.getProperty("REFERENCE_CODE"));
		// Ingrese aquí la descripción.
		parameters.put(PayU.PARAMETERS.DESCRIPTION, properties.getProperty("DESCRIPTION"));
		// Ingrese aquí el idima de la orden.
		parameters.put(PayU.PARAMETERS.LANGUAGE, properties.getProperty("LANGUAGE"));
		
		// Ingrese aquí el valor del IVA (Impuesto al Valor Agregado solo valido para
		// Colombia) de la transacción,
		// si se envía el IVA nulo el sistema aplicará el 19% automáticamente. Puede
		// contener dos dígitos decimales.
		// Ej: 19000.00. En caso de no tener IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_VALUE, properties.getProperty("TAX_VALUE"));
		
		// Ingrese aquí el valor base sobre el cual se calcula el IVA (solo valido para
		// Colombia).
		// En caso de que no tenga IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_RETURN_BASE, properties.getProperty("TAX_RETURN_BASE"));
		
		// Ingrese aquí la moneda.
		parameters.put(PayU.PARAMETERS.CURRENCY, "" + Currency.COP.name());

		// -- Comprador --
		// Ingrese aquí el id del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_ID, properties.getProperty("BUYER_ID"));
		
		// Ingrese aquí el nombre del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_NAME, properties.getProperty("BUYER_NAME"));
		
		// Ingrese aquí el email del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, properties.getProperty("BUYER_EMAIL"));
		// Ingrese aquí el teléfono de contacto del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_CONTACT_PHONE, properties.getProperty("BUYER_CONTACT_PHONE"));
		
		// Ingrese aquí el documento de contacto del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_DNI, properties.getProperty("BUYER_DNI"));
		
		// Ingrese aquí la dirección del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_STREET, properties.getProperty("BUYER_STREET"));
		parameters.put(PayU.PARAMETERS.BUYER_STREET_2, properties.getProperty("BUYER_STREET_2"));
		parameters.put(PayU.PARAMETERS.BUYER_CITY, properties.getProperty("BUYER_CITY"));
		parameters.put(PayU.PARAMETERS.BUYER_STATE, properties.getProperty("BUYER_STATE"));
		parameters.put(PayU.PARAMETERS.BUYER_COUNTRY, properties.getProperty("BUYER_COUNTRY"));
		parameters.put(PayU.PARAMETERS.BUYER_POSTAL_CODE, properties.getProperty("BUYER_POSTAL_CODE"));
		parameters.put(PayU.PARAMETERS.BUYER_PHONE, properties.getProperty("BUYER_PHONE"));
		
		return parameters;	
	}
	
	private List getPseBanks() {
		Map<String, String> parameters = new HashMap<>();
		
		//Ingrese aquí el nombre del medio de pago
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "PSE");
	
		//Ingrese aquí el nombre del pais.
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
	
	private void consignacion() {
		Map<String, String> parameters = buyerInformation();
		
		// -- Valores --
		//Ingrese aquí el valor de la transacción.
		parameters.put(PayU.PARAMETERS.VALUE, ""+value);
		
		//Ingrese aquí el nombre del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_NAME, "First name and second payer name");

		//Ingrese aquí el nombre del medio de pago en efectivo
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, payUConsignacionDTO.getMedioPago().getMessage());

		//Ingrese aquí el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		//Ingrese aquí la fecha de expiración.
		parameters.put(PayU.PARAMETERS.EXPIRATION_DATE,"2014-05-20T00:00:00");

		//IP del pagadador
		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "127.0.0.1");
		
		transactionBean.cargarBalance(numeroArboles);
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "billetera.xhtml");

		//Solicitud de autorización y captura
//		TransactionResponse response;
//		try {
//			instancePayU();
//			response = PayUPayments.doAuthorizationAndCapture(parameters);
//			
//			//Respuesta
//			if(response != null){
//				response.getOrderId();
//				response.getTransactionId();
//				response.getState();
//				if(response.getState().equals(TransactionState.PENDING)){
//					response.getPendingReason();
//					Map extraParameters = response.getExtraParameters();
//
//					//obtener la url del comprobante de pago
//					String url=(String)extraParameters.get("URL_PAYMENT_RECEIPT_HTML");
//				}
//				response.getPaymentNetworkResponseCode();
//				response.getPaymentNetworkResponseErrorMessage();
//				response.getTrazabilityCode();
//				response.getResponseCode();
//				response.getResponseMessage();
//				response.setResponseCode(TransactionResponseCode.APPROVED);
//				if(TransactionResponseCode.APPROVED.equals(response.getResponseCode())) {
//					
//				}
//			}
//		} catch (PayUException | InvalidParametersException | ConnectionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
	}
	
	private void instancePayU() {
		PayU.apiKey = "8KB94yZmS1xu19eF8aKGYG9Nd4"; //Ingresa aquí tu apiKey.
		PayU.apiLogin = "bYC5fH2p5T270G6"; //Ingresa aquí tu apiLogin.
		PayU.language = Language.es; //Ingresa aquí el idioma que prefieras.
		PayU.isTest = true; //Dejarlo verdadero cuando sean pruebas.
		LoggerUtil.setLogLevel(Level.ALL); //Incluirlo únicamente si desea ver toda la traza del log; si solo se desea ver la respuesta, se puede eliminar.
		PayU.paymentsUrl = "https://api.payulatam.com/payments-api/"; //Incluirlo únicamente si desea probar en un servidor de pagos específico, e indicar la ruta del mismo.
		PayU.reportsUrl = "https://api.payulatam.com/reports-api/"; //Incluirlo únicamente si desea probar en un servidor de reportes específico, e indicar la ruta del mismo
	}
	
	public void renderModal(String modal) {
		if(modal.equals("creditCard")) {
			setRenderCreditCard(Boolean.TRUE);
			setRenderPSE(Boolean.FALSE);
			setRenderConsignacion(Boolean.FALSE);
			setHeaderModal("Tarjeta de credito");
			
		} else if(modal.equals("pse")) {
			setRenderCreditCard(Boolean.FALSE);
			setRenderPSE(Boolean.TRUE);
			setRenderConsignacion(Boolean.FALSE);
			setHeaderModal("PSE");
			
		} else if(modal.equals("consignacion")) {
			setRenderCreditCard(Boolean.FALSE);
			setRenderPSE(Boolean.FALSE);
			setRenderConsignacion(Boolean.TRUE);
			setHeaderModal("Consignacion");
		}
	}
	
	public void actualizarMonto() {
		Integer valorDolar = Integer.parseInt(properties.getProperty("DOLLAR_COP_VALUE"));
		Double valorReal = Double.valueOf((numeroMonedas*2) * valorDolar);
		setValue(valorReal.toString());
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "paymentMenu/payment.xhtml");
	}
	
	public Integer getNumeroArboles() {
		return numeroArboles;
	}
	
	public void setNumeroArboles(Integer numeroArboles) {
		this.numeroArboles = numeroArboles;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getHeaderModal() {
		return headerModal;
	}
	
	public void setHeaderModal(String headerModal) {
		this.headerModal = headerModal;
	}

	public Boolean getRenderCreditCard() {
		return renderCreditCard;
	}

	public void setRenderCreditCard(Boolean renderCreditCard) {
		this.renderCreditCard = renderCreditCard;
	}

	public Boolean getRenderPSE() {
		return renderPSE;
	}

	public void setRenderPSE(Boolean renderPSE) {
		this.renderPSE = renderPSE;
	}

	public Boolean getRenderConsignacion() {
		return renderConsignacion;
	}

	public void setRenderConsignacion(Boolean renderConsignacion) {
		this.renderConsignacion = renderConsignacion;
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
	
	public Integer getNumeroMonedas() {
		return numeroMonedas;
	}
	
	public void setNumeroMonedas(Integer numeroMonedas) {
		this.numeroMonedas = numeroMonedas;
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
