/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.example.devicemanager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.devicemanager.scanner.ExtendedBluetoothDevice;
import com.example.devicemanager.scanner.ScannerFragment;
import com.example.devicemanager.usb.DevicesFragment;
import com.example.elogapp.R;
import com.example.elogapp.activity.ui.home.MacInputDialogActivity;
import com.example.elogapp.util.AppConstants;
import com.example.elogapp.util.AppUtils;
import com.example.elogapp.util.PreferenceHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import no.nordicsemi.android.ble.observer.ConnectionObserver;
import no.nordicsemi.android.log.ILogSession;
import no.nordicsemi.android.log.LocalLogSession;
import no.nordicsemi.android.log.Logger;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;


/**
 * <p>
 * The {@link BleProfileServiceReadyActivity} activity is designed to be the base class for profile activities that uses services in order to connect to the
 * device. When user press CONNECT button a service is created and the activity binds to it. The service tries to connect to the service and notifies the
 * activity using Local Broadcasts ({@link LocalBroadcastManager}). See {@link BleProfileService} for messages. If the device is not in range it will listen for
 * it and connect when it become visible. The service exists until user will press DISCONNECT button.
 * </p>
 * <p>
 * When user closes the activity (f.e. by pressing Back button) while being connected, the Service remains working. It's still connected to the device or still
 * listens for it. When entering back to the activity, activity will to bind to the service and refresh UI.
 * </p>
 */
@SuppressWarnings("unused")
public abstract class BleProfileServiceReadyActivity<E extends BleProfileService.LocalBinder> extends AppCompatActivity
		implements ScannerFragment.OnDeviceSelectedListener, DevicesFragment.OnUSBDeviceSelectedListener, ConnectionObserver {
//	private static final String TAG = AppModel.TAG;

	private static final String SIS_DEVICE_NAME = "device_name";
	private static final String SIS_DEVICE = "device";
	private static final String LOG_URI = "log_uri";
	protected static final int REQUEST_ENABLE_BT = 2;
	protected static final int REQUEST_SHOW_MAC_DIALOG = 3;
	//	private SharedPreference pref;
	private E mService;

	protected ExtendedFloatingActionButton mConnectButton;

	private ILogSession mLogSession;
	private BluetoothDevice mBluetoothDevice;
	private String mDeviceName;
	private UsbDevice mUsbDevice;
	private List<ScanResult> deviceList = new ArrayList<>();

	private final BroadcastReceiver mUSBEventReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			// Refresh Devices list
			FragmentManager fm = getSupportFragmentManager();
			Fragment frag = fm.findFragmentByTag(DevicesFragment.FRAG_TAG);
			if (frag != null) {
				DevicesFragment deviceFrag = (DevicesFragment) frag;
				deviceFrag.refresh();
			}
		}
	};

	private final BroadcastReceiver mCommonBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			// Check if the broadcast applies the connected device
			if (!isBroadcastForThisDevice(intent))
				return;
			BluetoothDevice bluetoothDevice = null;
			UsbDevice usbDevice = null;

			if (AppModel.MODE_USB) {
				usbDevice = intent.getParcelableExtra(BleProfileService.EXTRA_DEVICE);
			} else {
				bluetoothDevice = intent.getParcelableExtra(BleProfileService.EXTRA_DEVICE);
			}
			final String action = intent.getAction();
			switch (action) {
				case BleProfileService.BROADCAST_CONNECTION_STATE: {
					final int state = intent.getIntExtra(BleProfileService.EXTRA_CONNECTION_STATE, BleProfileService.STATE_DISCONNECTED);

					switch (state) {
						case BleProfileService.STATE_CONNECTED: {
							mDeviceName = intent.getStringExtra(BleProfileService.EXTRA_DEVICE_NAME);
							if (AppModel.MODE_USB) {
								onDeviceConnected(usbDevice);
							} else {
								onDeviceConnected(bluetoothDevice);
							}
							break;
						}
						case BleProfileService.STATE_DISCONNECTED: {

							if (AppModel.MODE_USB) {
								onDeviceDisconnected(usbDevice);
							} else {
								onDeviceDisconnected(bluetoothDevice, REASON_SUCCESS);
							}
							mDeviceName = null;
							break;
						}
						case BleProfileService.STATE_LINK_LOSS: {
							onLinkLossOccurred(bluetoothDevice);
							break;
						}

						case BleProfileService.STATE_DEVICE_NOT_SUPPORTED: {
							onDeviceNotSupported(bluetoothDevice);
							onDeviceDisconnected(bluetoothDevice, REASON_NOT_SUPPORTED);
							mDeviceName = null;

							break;
						}

						case BleProfileService.STATE_CONNECTING: {
							AppUtils.logger( "** Connecting from receiver **");
							onDeviceConnecting(bluetoothDevice);
							break;
						}
						case BleProfileService.STATE_DISCONNECTING: {
							onDeviceDisconnecting(bluetoothDevice);
							break;
						}
						default:
							// there should be no other actions
							break;
					}
					break;
				}
				case BleProfileService.BROADCAST_SERVICES_DISCOVERED: {
					final boolean primaryService = intent.getBooleanExtra(BleProfileService.EXTRA_SERVICE_PRIMARY, false);
					final boolean secondaryService = intent.getBooleanExtra(BleProfileService.EXTRA_SERVICE_SECONDARY, false);

					if (primaryService) {
						onServicesDiscovered(bluetoothDevice, secondaryService);
					} else {
						onDeviceNotSupported(bluetoothDevice);
					}
					break;
				}
				case BleProfileService.BROADCAST_DEVICE_READY: {
					onDeviceReady(bluetoothDevice);
					break;
				}
//				case BleProfileService.BROADCAST_BOND_STATE: {
//					final int state = intent.getIntExtra(BleProfileService.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
//					switch (state) {
//						case BluetoothDevice.BOND_BONDING:
//							onBondingRequired(bluetoothDevice);
//							break;
//						case BluetoothDevice.BOND_BONDED:
//							onBonded(bluetoothDevice);
//							break;
//					}
//					break;
//				}
//				case BleProfileService.BROADCAST_ERROR: {
//					final String message = intent.getStringExtra(BleProfileService.EXTRA_ERROR_MESSAGE);
//					final int errorCode = intent.getIntExtra(BleProfileService.EXTRA_ERROR_CODE, 0);
//					onError(bluetoothDevice, message, errorCode);
//					break;
//				}
				case BleProfileService.BROADCAST_FAILED_TO_CONNECT: {
					final int reason = intent.getIntExtra(BleProfileService.EXTRA_REASON, 0);
					onDeviceFailedToConnect(bluetoothDevice, reason);
					break;
				}
			}
		}
	};

//	ColorStateList connectedColorList()
//	{
//		// Color for the action to be performed
//		int[][] states = new int[][] {
//				new int[] { android.R.attr.state_enabled}, // enabled
//				new int[] { android.R.attr.state_pressed}  // pressed
//		};
//
//		int[] colors = new int[] {
//				0xff9800,
//				0xfb8c00,
//		};
//
//		return new ColorStateList(states, colors);
//	}
//
//	ColorStateList disconnectedColorList()
//	{
//		// Color for the action to be performed
//		int[][] states = new int[][] {
//				new int[] { android.R.attr.state_active}, // active
//				new int[] { android.R.attr.state_enabled}, // enabled
//				new int[] { android.R.attr.state_pressed}  // pressed
//		};
//
//		int[] colors = new int[] {
//				0xc0ca33, // Lime 600
//				0xcddc39, // Lime 500
//				0xc0ca33, // Lime 600
//		};
//
//		return new ColorStateList(states, colors);
//	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@SuppressWarnings("unchecked")
		@Override
		public void onServiceConnected(final ComponentName name, final IBinder service) {
			final E bleService = mService = (E) service;
			mBluetoothDevice = bleService.getBluetoothDevice();
			mLogSession = mService.getLogSession();

			Logger.d(mLogSession, "Activity bound to the service");

			if (service == null) {
				return;
			}

			onServiceBound(bleService);

			// Update UI
			mDeviceName = bleService.getDeviceName();
			//mDeviceNameView.setText(mDeviceName);
			//mConnectButton.setBackgroundTintList(connectedColorList());

			// And notify user if device is connected
			if (bleService.isConnected()) {
				onDeviceConnected(mBluetoothDevice);
			} else {
				// If the device is not connected it means that either it is still connecting,
				// or the link was lost and service is trying to connect to it (autoConnect=true).
				AppUtils.logger( "** Connecting from bind **");
				onDeviceConnecting(mBluetoothDevice);
			}
		}

		@Override
		public void onServiceDisconnected(final ComponentName name) {
			// Note: this method is called only when the service is killed by the system,
			// not when it stops itself or is stopped by the activity.
			// It will be called only when there is critically low memory, in practice never
			// when the activity is in foreground.
			Logger.d(mLogSession, "Activity disconnected from the service");
			//mDeviceNameView.setText(getDefaultDeviceName());
			//mConnectButton.setBackgroundTintList(disconnectedColorList());

			mService = null;
			mDeviceName = null;
			mBluetoothDevice = null;
			mLogSession = null;
			onServiceUnbound();
		}
	};

	@Override
	protected final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		pref = new SharedPreference(this);
//		boolean featureEld = pref.getBoolean(AppConstants.PREF_FEATURE_ELD, false);

//		if(featureEld)
//			return;


		AppUtils.logger( "Bluetooth: on created.");
		ensureBLESupported();
		if (!isBLEEnabled()) {
			AppUtils.logger( "Bluetooth: is off");
			showBLEDialog();
		}else{
			AppUtils.logger( "Bluetooth: is on");
			String dName = new PreferenceHelper().getString(this, AppConstants.PREF_DEVICE_NAME);
			String macId = new PreferenceHelper().getString(this, AppConstants.PREF_DEVICE_ID_MAC);

			if (!dName.isEmpty() && !macId.isEmpty()) {
				startScan();
			}
		}

		// Restore the old log session
		if (savedInstanceState != null) {
			final Uri logUri = savedInstanceState.getParcelable(LOG_URI);
			mLogSession = Logger.openSession(getApplicationContext(), logUri);
		}

		// In onInitialize method a final class may register local broadcast receivers that will listen for events from the service
		onInitialize(savedInstanceState);
		// The onCreateView class should... create the view
		onCreateView(savedInstanceState);

		// view references are obtained here
		setUpView();
		// View is ready to be used
		onViewCreated(savedInstanceState);

		LocalBroadcastManager.getInstance(this).registerReceiver(mCommonBroadcastReceiver, makeIntentFilter());

		final IntentFilter usbEvFilter = new IntentFilter();
		usbEvFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		registerReceiver(mUSBEventReceiver, usbEvFilter);
		
	}

	@Override
	protected void onStart() {
		super.onStart();

		/*
		 * If the service has not been started before, the following lines will not start it.
		 * However, if it's running, the Activity will bind to it and notified via mServiceConnection.
		 */
		final Intent service = new Intent(this, getServiceClass());
		// We pass 0 as a flag so the service will not be created if not exists.
		bindService(service, mServiceConnection, 0);

		/*
		 * When user exited the TrackerManagerActivity while being connected, the log session is kept in
		 * the service. We may not get it before binding to it so in this case this event will
		 * not be logged (mLogSession is null until onServiceConnected(..) is called).
		 * It will, however, be logged after the orientation changes.
		 */
	}

	@Override
	protected void onStop() {
		super.onStop();

		try {
			// We don't want to perform some operations (e.g. disable Battery Level notifications)
			// in the service if we are just rotating the screen. However, when the activity will
			// disappear, we may want to disable some device features to reduce the battery
			// consumption.
			if (mService != null)
				mService.setActivityIsChangingConfiguration(isChangingConfigurations());

			unbindService(mServiceConnection);
			mService = null;

			Logger.d(mLogSession, "Activity unbound from the service");
			onServiceUnbound();
			mDeviceName = null;
			mBluetoothDevice = null;
			mLogSession = null;
		} catch (final IllegalArgumentException e) {
			// do nothing, we were not connected to the sensor
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppUtils.logger( "Bluetooth: : destroyed.");
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mCommonBroadcastReceiver);
		unregisterReceiver(mUSBEventReceiver);
	}

	private static IntentFilter makeIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BleProfileService.BROADCAST_CONNECTION_STATE);
		//intentFilter.addAction(BleProfileService.BROADCAST_SERVICES_DISCOVERED);
		intentFilter.addAction(BleProfileService.BROADCAST_DEVICE_READY);
		//intentFilter.addAction(BleProfileService.BROADCAST_BOND_STATE);
		//intentFilter.addAction(BleProfileService.BROADCAST_ERROR);
		intentFilter.addAction(BleProfileService.BROADCAST_FAILED_TO_CONNECT);
		return intentFilter;
	}

	/**
	 * Called when activity binds to the service. The parameter is the object returned in {@link Service#onBind(Intent)} method in your service. The method is
	 * called when device gets connected or is created while sensor was connected before. You may use the binder as a sensor interface.
	 */
	protected abstract void onServiceBound(E binder);

	/**
	 * Called when activity unbinds from the service. You may no longer use this binder because the sensor was disconnected. This method is also called when you
	 * leave the activity being connected to the sensor in the background.
	 */
	protected abstract void onServiceUnbound();

	/**
	 * Returns the service class for sensor communication. The service class must derive from {@link BleProfileService} in order to operate with this class.
	 *
	 * @return the service class
	 */
	protected abstract Class<? extends BleProfileService> getServiceClass();

	/**
	 * Returns the service interface that may be used to communicate with the sensor. This will return <code>null</code> if the device is disconnected from the
	 * sensor.
	 *
	 * @return the service binder or <code>null</code>
	 */
	protected E getService() {
		return mService;
	}

	/**
	 * You may do some initialization here. This method is called from {@link #onCreate(Bundle)} before the view was created.
	 */
	protected void onInitialize(final Bundle savedInstanceState) {
		// empty default implementation
	}

	/**
	 * Called from {@link #onCreate(Bundle)}. This method should build the activity UI, i.e. using {@link #setContentView(int)}.
	 * Use to obtain references to views. Connect/Disconnect button, the device name view are manager automatically.
	 *
	 * @param savedInstanceState contains the data it most recently supplied in {@link #onSaveInstanceState(Bundle)}.
	 *                           Note: <b>Otherwise it is null</b>.
	 */
	protected abstract void onCreateView(final Bundle savedInstanceState);

	/**
	 * Called after the view has been created.
	 *
	 * @param savedInstanceState contains the data it most recently supplied in {@link #onSaveInstanceState(Bundle)}.
	 *                           Note: <b>Otherwise it is null</b>.
	 */
	protected void onViewCreated(final Bundle savedInstanceState) {
		// empty default implementation
	}

	/**
	 * Called after the view and the toolbar has been created.
	 */
	protected final void setUpView() {
		// set GUI
		mConnectButton = findViewById(getConnectionToggleResourceId());
		//mConnectButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabConnect)));
		mConnectButton.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_bluetooth));

		mConnectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onConnectClicked(v);
			}
		});

		if (AppModel.MODE_USB) {
			mConnectButton.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(SIS_DEVICE_NAME, mDeviceName);
		outState.putParcelable(SIS_DEVICE, mBluetoothDevice);
		if (mLogSession != null)
			outState.putParcelable(LOG_URI, mLogSession.getSessionUri());
	}

	@Override
	protected void onRestoreInstanceState(final @NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mDeviceName = savedInstanceState.getString(SIS_DEVICE_NAME);
		mBluetoothDevice = savedInstanceState.getParcelable(SIS_DEVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
//		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	/**
	 * Use this method to handle menu actions other than home and about.
	 *
	 * @param itemId the menu item id
	 * @return <code>true</code> if action has been handled
	 */
	protected boolean onOptionsItemSelected(final int itemId) {
		// Overwrite when using menu other than R.menu.help
		return false;
	}

//	@Override
//	public boolean onOptionsItemSelected(final MenuItem item) {
//		final int id = item.getItemId();
//		switch (id) {
//			case android.R.id.home:
//				onBackPressed();
//				break;
////			case R.id.action_about:
////				final AppHelpFragment fragment = AppHelpFragment.getInstance(getAboutTextId());
////				fragment.show(getSupportFragmentManager(), "help_fragment");
////				break;
//			default:
//				return onOptionsItemSelected(id);
//		}
//		return true;
//	}


	/**
	 * Called when user press CONNECT or DISCONNECT button. See layout files -> onClick attribute.
	 * The view processing occurs in the derived Activity per the Service events
	 */
	public void onConnectClicked(final View view) {

		if (AppModel.MODE_USB) {
			if ((mService != null)) {
				// Fab is only visible in the 'connected' state
				mService.disconnect(this);
			}
		} else {

			if (isBLEEnabled()) {//C2:49

				String dName = new PreferenceHelper().getString(getApplicationContext(), AppConstants.PREF_DEVICE_NAME);
				String macId = new PreferenceHelper().getString(getApplicationContext(), AppConstants.PREF_DEVICE_ID_MAC);

				if ((mService == null)) {

					if(/*!dName.isEmpty() &&*/ !macId.isEmpty()) {

						final Intent enableIntent = new Intent(this, MacInputDialogActivity.class);
						startActivityForResult(enableIntent, REQUEST_SHOW_MAC_DIALOG);
						//startScan();
						return;
					}

					final Intent enableIntent = new Intent(this, MacInputDialogActivity.class);
					startActivityForResult(enableIntent, REQUEST_SHOW_MAC_DIALOG);

//					AppUtils.logger( "mService status null");
//					setDefaultUI();
//					showDeviceScanningDialog(getFilterUUID());
				} else {
					AppUtils.logger( "mService status not null " + mService.getConnectionState());
					switch (mService.getConnectionState()) {
						case BluetoothProfile.STATE_CONNECTING:
						case BluetoothProfile.STATE_CONNECTED:
							AppUtils.logger( "Service Disconnected");
							mService.disconnect(this);
							break;
						default:
							AppUtils.logger( "mService status not null default execute");
							setDefaultUI();
							showDeviceScanningDialog(getFilterUUID());
					}
				}
			}
			else {
				showBLEDialog();
			}
		}
	}

	/**
	 * Fired when user selected the device.
	 *
	 * @param device
	 *            the device to connect to
	 * @param name
	 *            the device name. Unfortunately on some devices {@link BluetoothDevice#getName()}
	 *            always returns <code>null</code>, i.e. Sony Xperia Z1 (C6903) with Android 4.3.
	 *            The name has to be parsed manually form the Advertisement packet.
	 */
	public void onDeviceSelected(final BluetoothDevice device, final String name) {

		AppUtils.logger("Bluetooth: onDeviceSelected called");
		final int titleId = getLoggerProfileTitle();
		if (titleId > 0) {
			mLogSession = Logger.newSession(getApplicationContext(), getString(titleId), device.getAddress(), name);
			// If nRF Logger is not installed we may want to use local logger
			if (mLogSession == null && getLocalAuthorityLogger() != null) {
				mLogSession = LocalLogSession.newSession(getApplicationContext(), getLocalAuthorityLogger(), device.getAddress(), name);
			}
		}
		mBluetoothDevice = device;
		mDeviceName = name;

		// The device may not be in the range but the service will try to connect to it if it reach it
		AppUtils.logger( "Bluetooth: : Starting service...");
		Logger.d(mLogSession, "Starting service...");
		final Intent service = new Intent(this, getServiceClass());
		service.putExtra(BleProfileService.EXTRA_DEVICE_ADDRESS, device.getAddress());
		service.putExtra(BleProfileService.EXTRA_DEVICE_NAME, name);
		if (mLogSession != null)
			service.putExtra(BleProfileService.EXTRA_LOG_URI, mLogSession.getSessionUri());

		startService(service);
		Logger.d(mLogSession, "Bluetooth: Binding to the service...");
		AppUtils.logger( "Bluetooth: : Binding to Service ...");
		bindService(service, mServiceConnection, 0);

		AppUtils.logger("onDeviceSelected finish");

	}

	/**
	 * Fired when user selected the device.
	 *
	 * @param device
	 *            the device to connect to
	 * @param name
	 */
	public void onUsbDeviceSelected(final UsbDevice device, final String name, final int portnum) {

		mUsbDevice = device;
		mDeviceName = name;

		AppUtils.logger( "BA: Starting service (U)...");
		final Intent service = new Intent(this, getServiceClass());
		service.putExtra(BleProfileService.EXTRA_DEVICE_ADDRESS, device.getDeviceId());
		service.putExtra(BleProfileService.EXTRA_DEVICE_PORTNUM, portnum);
		service.putExtra(BleProfileService.EXTRA_DEVICE_NAME, name);

		startService(service);
		AppUtils.logger( "BA: Binding to Service (U)...");
		bindService(service, mServiceConnection, 0);
	}


	/**
	 * Returns the title resource id that will be used to create logger session. If 0 is returned (default) logger will not be used.
	 *
	 * @return the title resource id
	 */
	protected int getLoggerProfileTitle() {
		return 0;
	}

	/**
	 * This method may return the local log content provider authority if local log sessions are supported.
	 *
	 * @return local log session content provider URI
	 */
	protected Uri getLocalAuthorityLogger() {
		return null;
	}


	@Override
	public void onDeviceConnecting(final BluetoothDevice device) {
		mConnectButton.setText(R.string.connecting);
		mConnectButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabConnecting)));
	}

	@Override
	public void onDeviceConnected(final BluetoothDevice device) {
		mConnectButton.setText(R.string.disconnect);
		mConnectButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabDisconnect)));
	}

	public void onDeviceConnected(final UsbDevice device) {
		mConnectButton.setText(R.string.disconnect);
		mConnectButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabDisconnect)));
	}


	@Override
	public void onDeviceDisconnecting(final BluetoothDevice device) {
		mConnectButton.setText(R.string.disconnecting);
		mConnectButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabDisconnecting)));
	}


	public void onDeviceDisconnected(final UsbDevice device) {
		mConnectButton.setText(R.string.connect);
		mConnectButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabConnect)));

		AppUtils.logger("Bluetooth: USB Device disconnected. Unbinding from the service...");

		try {
			unbindService(mServiceConnection);
			mService = null;

			onServiceUnbound();
			mDeviceName = null;

			if (AppModel.MODE_USB) {
				mConnectButton.setVisibility(View.INVISIBLE);
			}

		} catch (final IllegalArgumentException e) {
			// do nothing. This should never happen but does...
		}
	}

	@Override
	public void onDeviceDisconnected(final BluetoothDevice device, final int reason) {
		AppUtils.logger( "Bluetooth Device Disconnected");
		mConnectButton.setText(R.string.connect);
		mConnectButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabConnect)));

		AppUtils.logger( "Bluetooth: BLE device disconnected. Unbinding from the service...");

		try {
			Logger.d(mLogSession, "Unbinding from the service...");
			unbindService(mServiceConnection);
			mService = null;

			Logger.d(mLogSession, "Activity unbound from the service");
			onServiceUnbound();
			mDeviceName = null;
			mBluetoothDevice = null;
			mLogSession = null;
		} catch (final IllegalArgumentException e) {
			// do nothing. This should never happen but does...
		}
	}


	public void onDeviceNotSupported(final BluetoothDevice device) {
		showToast("BA: Not Supported :" + device.getAddress());
	}


	public void onLinkLossOccurred(final BluetoothDevice device) {
		mConnectButton.setText(R.string.connecting);
		mConnectButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabConnecting)));
	}

	public void onServicesDiscovered(final BluetoothDevice device, final boolean optionalServicesFound) {
		// empty default implementation
	}

	@Override
	public void onDeviceReady(final BluetoothDevice device) {
		// empty default implementation
	}


	/**
	 * Shows a message as a Toast notification. This method is thread safe, you can call it from any thread
	 *
	 * @param message a message to be shown
	 */
	protected void showToast(final String message) {
		runOnUiThread(() -> Toast.makeText(BleProfileServiceReadyActivity.this, message, Toast.LENGTH_LONG).show());
	}

	/**
	 * Shows a message as a Toast notification. This method is thread safe, you can call it from any thread
	 *
	 * @param messageResId an resource id of the message to be shown
	 */
	protected void showToast(final int messageResId) {
		runOnUiThread(() -> Toast.makeText(BleProfileServiceReadyActivity.this, messageResId, Toast.LENGTH_SHORT).show());
	}

	/**
	 * Returns <code>true</code> if the device is connected. Services may not have been discovered yet.
	 */
	protected boolean isDeviceConnected() {
		return mService != null && mService.isConnected();
	}

	/**
	 * Returns the name of the device that the phone is currently connected to or was connected last time
	 */
	protected String getDeviceName() {
		return mDeviceName;
	}

	/**
	 * Returns the resource id of the button which toggles the connection
	 */
	protected abstract int getConnectionToggleResourceId();

	/**
	 * Restores the default UI before reconnecting
	 */
	protected abstract void setDefaultUI();


	/**
	 * The UUID filter is used to filter out available devices that does not have such UUID in their advertisement packet. See also:
	 * {@link #isChangingConfigurations()}.
	 *
	 * @return the required UUID or <code>null</code>
	 */
	protected abstract UUID getFilterUUID();

	/**
	 * Checks the {@link BleProfileService#EXTRA_DEVICE} in the given intent and compares it with the connected BluetoothDevice object.
	 * @param intent intent received via a broadcast from the service
	 * @return true if the data in the intent apply to the connected device, false otherwise
	 */
	protected boolean isBroadcastForThisDevice(final Intent intent) {
		if (AppModel.MODE_USB) {
			final UsbDevice usbDevice = intent.getParcelableExtra(BleProfileService.EXTRA_DEVICE);
			return usbDevice != null;
		} else {
			final BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BleProfileService.EXTRA_DEVICE);
			return mBluetoothDevice != null && mBluetoothDevice.equals(bluetoothDevice);
		}
	}

	/**
	 * Shows the scanner fragment.
	 *
	 * @param filter               the UUID filter used to filter out available devices. The fragment will always show all bonded devices as there is no information about their
	 *                             services
	 * @see #getFilterUUID()
	 */
	private void showDeviceScanningDialog(final UUID filter) {
		final ScannerFragment dialog = ScannerFragment.getInstance(filter);
		dialog.show(getSupportFragmentManager(), "scan_fragment");
	}

	/**
	 * Returns the log session. Log session is created when the device was selected using the and released when user press DISCONNECT.
	 *
	 * @return the logger session or <code>null</code>
	 */
	protected ILogSession getLogSession() {
		return mLogSession;
	}

	private void ensureBLESupported() {
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "BA: BLE is not supported!", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	protected boolean isBLEEnabled() {

		AppUtils.logger( "Bluetooth: isBLEEnabled method");
		final BluetoothManager bluetoothManager = (BluetoothManager)
				getSystemService(Context.BLUETOOTH_SERVICE);
		final BluetoothAdapter adapter = bluetoothManager.getAdapter();
		return adapter != null && adapter.isEnabled();
	}

	protected void showBLEDialog() {
		final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Toast.makeText(this, "BA: BLE is not supported!" + requestCode + " " + resultCode, Toast.LENGTH_LONG).show();
		if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ENABLE_BT) {

			mConnectButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFabConnect)));

			String macId = new PreferenceHelper().getString(this,AppConstants.PREF_DEVICE_ID_MAC);
			if(macId.isEmpty()){

				final Intent enableIntent = new Intent(this, MacInputDialogActivity.class);
				startActivityForResult(enableIntent, REQUEST_SHOW_MAC_DIALOG);

//				setDefaultUI();
//				showDeviceScanningDialog(getFilterUUID());
			}else{
				startScan();
			}
		}

		if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SHOW_MAC_DIALOG) {

			String macId = new PreferenceHelper().getString(getApplicationContext(), AppConstants.PREF_DEVICE_ID_MAC);
			if(!macId.isEmpty()){
				new PreferenceHelper().save(getApplicationContext(), AppConstants.PREF_DEVICE_ID_MAC, macId);
				startScan();
			}


		}

		if (resultCode == Activity.RESULT_CANCELED && requestCode == REQUEST_ENABLE_BT) {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					if (!isFinishing()){
						new AlertDialog.Builder(BleProfileServiceReadyActivity.this)
								.setTitle("Alert")
								.setMessage("Bluetooth service is mandatory to run application without it you can't continue")
								.setCancelable(false)
								.setPositiveButton("OK", (dialog, which) -> {
									finishAffinity();
								}).show();
					}
				}
			});

		}
	}

	@Override
	public void onDialogCanceled() {

	}

	private final static int REQUEST_PERMISSION_REQ_CODE = 34; // any 8-bit number
	private boolean mIsScanning = false;
	private final static long SCAN_DURATION = 5000;
	private final Handler mHandler = new Handler();

	/**
	 * Scan for 5 seconds and then stop scanning when a BluetoothLE device is found then mLEScanCallback
	 * is activated This will perform regular scan for custom BLE Service UUID and then filter out.
	 * using class ScannerServiceParser
	 */
	public void startScan() {

		AppUtils.logger("Bluetooth: Start Scan");
		// Since Android 6.0 we need to obtain either Manifest.permission.ACCESS_COARSE_LOCATION or Manifest.permission.ACCESS_FINE_LOCATION to be able to scan for
		// Bluetooth LE devices. This is related to beacons as proximity devices.
		// On API older than Marshmallow the following code does nothing.
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// When user pressed Deny and still wants to use this functionality, show the rationale
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) /*&& mPermissionRationale.getVisibility() == View.GONE*/) {
//				mPermissionRationale.setVisibility(View.VISIBLE);
				return;
			}

			requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
			return;
		}

		// Hide the rationale message, we don't need it anymore.
//		if (mPermissionRationale != null)
//			mPermissionRationale.setVisibility(View.GONE);

//		mAdapter.clearDevices();
//		mScanButton.setText(R.string.scanner_action_cancel);

		final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
		final ScanSettings settings = new ScanSettings.Builder()
				.setLegacy(false)
				.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
				.setReportDelay(3000)
				.setUseHardwareBatchingIfSupported(false)
				.build();
		final List<ScanFilter> filters = new ArrayList<>();
		filters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(getFilterUUID())).build());
		scanner.startScan(filters, settings, scanCallback);

		mIsScanning = true;
		mHandler.postDelayed(() -> {
			if (mIsScanning) {
				stopScan();
			}
		}, SCAN_DURATION);
	}


		 private ScanCallback scanCallback = new ScanCallback() {
			 @Override
			 public void onScanResult(final int callbackType, final ScanResult result) {
				 // do nothing
			 }

			 @Override
			 public void onBatchScanResults(final List<ScanResult> results) {

				 deviceList = results;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "Scan Complete", Toast.LENGTH_LONG).show();
							AppUtils.logger("Bluetooth: Scan Complete");
//							update(results);
						}
					});

				 update(results);


			 }

			 @Override
			 public void onScanFailed(final int errorCode) {
				 // should never be called

				 runOnUiThread(new Runnable() {
					 @Override
					 public void run() {
						 Toast.makeText(getApplicationContext(), "Scan failed", Toast.LENGTH_LONG).show();
					 }
				 });

				 AppUtils.logger("Bluetooth Scan failed");

			 }
		 };


	private final ArrayList<ExtendedBluetoothDevice> mListBondedValues = new ArrayList<>();
	private final ArrayList<ExtendedBluetoothDevice> mListValues = new ArrayList<>();
	/**
	 * Updates the list of not bonded devices.
	 * @param results list of results from the scanner
	 */
	public void update(final List<ScanResult> results) {
		AppUtils.logger("Bluetooth: Update call ");
		for (final ScanResult result : results) {
			AppUtils.logger("Bluetooth: total device found" + results.size());
			final ExtendedBluetoothDevice device = findDevice(result);
			if (device == null) {
				AppUtils.logger("Bluetooth: device null found" );
				mListValues.add(new ExtendedBluetoothDevice(result));
			} else {
				AppUtils.logger("Bluetooth: device not found" );

				String dName = result.getScanRecord().getDeviceName();//new SharedPreference(this).getString(AppConstants.PREF_DEVICE_NAME);
				String macIdPref = new PreferenceHelper().getString(this, AppConstants.PREF_DEVICE_ID_MAC).toUpperCase(Locale.ROOT);


				device.name = result.getScanRecord() != null ? result.getScanRecord().getDeviceName() : null;
				device.rssi = result.getRssi();

				AppUtils.logger("Bluetooth: Device Found" + result.getDevice().getAddress());

				String macId = result.getDevice().getAddress().toUpperCase().replace(":","");
				if(macId.contains(macIdPref)){
					AppUtils.logger("Bluetooth: Device Found");
					new PreferenceHelper().save(this,AppConstants.PREF_DEVICE_ID_MAC, macIdPref);

					onDeviceSelected(result.getDevice(), dName);
					break;
				}

				AppUtils.logger("Bluetooth: Update call completed");

			}
		}
	}

	private ExtendedBluetoothDevice findDevice(final ScanResult result) {
		for (final ExtendedBluetoothDevice device : mListBondedValues)
			if (device.matches(result))
				return device;
		for (final ExtendedBluetoothDevice device : mListValues)
			if (device.matches(result))
				return device;
		return null;
	}


	/**
	 * Stop scan if user tap Cancel button
	 */
	private void stopScan() {
		if (mIsScanning) {
//			mScanButton.setText(R.string.scanner_action_scan);

			final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
			scanner.stopScan(scanCallback);

			mIsScanning = false;
		}
	}

}
